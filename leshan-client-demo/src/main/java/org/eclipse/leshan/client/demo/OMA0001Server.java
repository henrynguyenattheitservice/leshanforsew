/*******************************************************************************
 * Copyright (c) 2022    Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.leshan.client.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.client.servers.ServerIdentity;
import org.eclipse.leshan.core.Destroyable;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.model.ResourceModel;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.request.BindingMode;
import org.eclipse.leshan.core.request.argument.Arguments;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OMA0001Server extends BaseInstanceEnabler implements Destroyable {

    private static final Logger LOG = LoggerFactory.getLogger(OMA0001Server.class);
    private final static List<Integer> supportedResources = Arrays.asList(0, 1, 2, 3, 5, 6, 7, 8);
    public static final String INVALID_TYPE = "invalid type";
    public static final String INVALID_VALUE = "invalid value";

    private int shortServerId;
    private long lifetime;
    private Long defaultMinPeriod;
    private Long defaultMaxPeriod;
    private BindingMode binding;
    private long disableTimeout;
    private boolean notifyWhenDisable;

    private Timer timer;

    public OMA0001Server() {
        // notify new date each 5 second
        this.timer = new Timer("Server-Current Time");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fireResourceChange(13);
            }
        }, 5000, 5000);
    }

    @Override
    public ReadResponse read(ServerIdentity identity, int resourceid) {

        switch (resourceid) {
        case 0: // short server ID
            return ReadResponse.success(resourceid, shortServerId);

        case 1: // lifetime
            return ReadResponse.success(resourceid, lifetime);

        case 2: // default min period
            if (null == defaultMinPeriod)
                return ReadResponse.notFound();
            return ReadResponse.success(resourceid, defaultMinPeriod);

        case 3: // default max period
            if (null == defaultMaxPeriod)
                return ReadResponse.notFound();
            return ReadResponse.success(resourceid, defaultMaxPeriod);

        case 5: // disable timeout
            return ReadResponse.success(resourceid, disableTimeout);

        case 6: // notification storing when disable or offline
            return ReadResponse.success(resourceid, notifyWhenDisable);

// binding - not defined check with IPSO OMA
//      case 7:
//            return ReadResponse.success(resourceid, binding.toString());

        default:
            return super.read(identity, resourceid);
        }
    }

    @Override
    public WriteResponse write(ServerIdentity identity, boolean replace, int resourceid, LwM2mResource value) {

        switch (resourceid) {

        case 0:
            if (value.getType() != ResourceModel.Type.INTEGER) {
                return WriteResponse.badRequest(INVALID_TYPE);
            }
            shortServerId = ((Long) value.getValue()).intValue();
            return WriteResponse.success();

        case 1:
            if (value.getType() != ResourceModel.Type.INTEGER) {
                return WriteResponse.badRequest(INVALID_TYPE);
            }
            lifetime = (Long) value.getValue();
            return WriteResponse.success();

        case 2:
            if (value.getType() != ResourceModel.Type.INTEGER) {
                return WriteResponse.badRequest(INVALID_TYPE);
            }
            defaultMinPeriod = (Long) value.getValue();
            return WriteResponse.success();

        case 3:
            if (value.getType() != ResourceModel.Type.INTEGER) {
                return WriteResponse.badRequest(INVALID_TYPE);
            }
            defaultMaxPeriod = (Long) value.getValue();
            return WriteResponse.success();

        case 5:
            if (value.getType() != ResourceModel.Type.INTEGER) {
                return WriteResponse.badRequest(INVALID_TYPE);
            }
            defaultMaxPeriod = (Long) value.getValue();
            return WriteResponse.success();

        case 6: // notification storing when disable or offline
            if (value.getType() != ResourceModel.Type.BOOLEAN) {
                return WriteResponse.badRequest(INVALID_TYPE);
            }
            notifyWhenDisable = (boolean) value.getValue();
            return WriteResponse.success();

        case 7: // binding
            if (value.getType() != ResourceModel.Type.STRING) {
                return WriteResponse.badRequest(INVALID_TYPE);
            }
            try {
                binding = BindingMode.valueOf((String) value.getValue());
                return WriteResponse.success();
            } catch (IllegalArgumentException e) {
                return WriteResponse.badRequest(INVALID_VALUE);
            }

        default:
            return super.write(identity, replace, resourceid, value);
        }
    }

    @Override
    public ExecuteResponse execute(ServerIdentity identity, int resourceid, Arguments params) {
        if (resourceid == 4) {
            new Timer("Reboot Lwm2mClient").schedule(new TimerTask() {
                @Override
                public void run() {
                    getLwM2mClient().stop(true);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    getLwM2mClient().start();
                }
            }, 500);
        }
        return super.execute(identity, resourceid, params);
    }

    @Override
    public void reset(int resourceid) {
        switch (resourceid) {
        case 2:
            defaultMinPeriod = null;
            break;
        case 3:
            defaultMaxPeriod = null;
            break;
        default:
            super.reset(resourceid);
        }
    }

    @Override
    public List<Integer> getAvailableResourceIds(ObjectModel model) {
        return supportedResources;
    }

    @Override
    public void destroy() {
        timer.cancel();
    }
}
