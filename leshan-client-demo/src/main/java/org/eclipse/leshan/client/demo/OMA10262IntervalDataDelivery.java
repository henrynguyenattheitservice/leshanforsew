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

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.client.servers.ServerIdentity;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;
import org.eclipse.leshan.core.util.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OMA10262IntervalDataDelivery extends BaseInstanceEnabler {
    private static final Logger LOG = LoggerFactory.getLogger(OMA10262IntervalDataDelivery.class);

    private static final int NAME = 0; //RW
    private static final int INTERVAL_DATA_LINKS = 1; //RW
    private static final int LATEST_PAYLOAD = 2; //R
    private static final int SCHEDULE = 3; //RW

    private final ScheduledExecutorService scheduler;
    private final double currentTemp = 20d;
    private final Random rng = new Random();
    private byte[] dlData;
    public OMA10262IntervalDataDelivery() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("OMA10250AppDataContainer"));
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                adjustIntervalData();
            }
        }, 0, 80, TimeUnit.SECONDS);
    }

    private synchronized byte[] adjustIntervalDataLink(double currentTemp) {
        return new byte[]{};
    }

    private synchronized void adjustIntervalData() {
//        float delta = (rng.nextInt(20) - 10) / 10f;
//        currentTemp += delta;
//        byte[] changedResource = adjustIntervalDataLink(currentTemp);
        fireResourceInstanceChange(LATEST_PAYLOAD, 2);
    }

    public byte[] getULData() {
        String eventLogBuffer = "8319281a01831a5a983fa0193840861903f31901c50c18391908ae190566";
        return eventLogBuffer.getBytes();
    }

    public byte[] getDLData() {
        String eventLogBuffer = "8319281a01831a5a983fa0193840861903f31901c50c18391908ae190566";
        return eventLogBuffer.getBytes();
    }

    public void setDLData(byte[] value) {
        dlData = value;
    }

    @Override
    public ReadResponse read(ServerIdentity identity, int resourceId) {
        LOG.info("Read on Device Resource " + resourceId);
        switch (resourceId) {
            case NAME:
                return ReadResponse.success(resourceId, getULData());
            case INTERVAL_DATA_LINKS:
                return ReadResponse.success(resourceId, getDLData());
            case LATEST_PAYLOAD:
                return ReadResponse.success(resourceId, getDLData());
            case SCHEDULE:
                return ReadResponse.success(resourceId, getDLData());
            default:
                return super.read(identity, resourceId);
        }
    }

    @Override
    public WriteResponse write(ServerIdentity identity, boolean replace, int resourceId, LwM2mResource value) {
        LOG.info("Write on Device Resource " + resourceId);
        switch (resourceId) {
            case NAME:
                setDLData((byte[]) value.getValue());
                return WriteResponse.success();
            case INTERVAL_DATA_LINKS:
                setDLData((byte[]) value.getValue());
                return WriteResponse.success();
            case SCHEDULE:
                setDLData((byte[]) value.getValue());
                return WriteResponse.success();
            default:
                return super.write(identity, replace, resourceId, value);
        }
    }


}

