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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OMA10264DeliverySchedule extends BaseInstanceEnabler {
    private static final Logger LOG = LoggerFactory.getLogger(OMA10264DeliverySchedule.class);
    private static final int SCHEDULE_START_TIME = 0; //RW
    private static final int SCHEDULE_UTC_OFFSET = 1; //RW
    private static final int DELIVERY_FREQUENCY = 2; //RW
    private static final int RANDOMISED_DELIVERY_WINDOW = 3; //RW
    private static final int NUMBER_OF_RETRIES = 4; //RW
    private static final int RETRY_PERIOD = 5; //RW

    private final ScheduledExecutorService scheduler;


    public OMA10264DeliverySchedule() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("OMA10264DeliverySchedule"));
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                adjustIntervalData();
            }
        }, 0, 80, TimeUnit.SECONDS);
    }

    private synchronized byte[] adjustIntervalDataLink(double currentTemp) {
        return new byte[] {};
    }
    private double currentTemp = 20d;
    private final Random rng = new Random();
    private synchronized void adjustIntervalData() {
//        float delta = (rng.nextInt(20) - 10) / 10f;
//        currentTemp += delta;
//        byte[] changedResource = adjustIntervalDataLink(currentTemp);
        fireResourceInstanceChange(DELIVERY_FREQUENCY, 2);
    }

    int scheduleStartTime;
    int deliveryFrequency;
    int radomisedDeliveryWindow;
    int numberOfRetries;
    int retryPeriod;

    private String scheduleUtcOffset = new SimpleDateFormat("X").format(Calendar.getInstance().getTime());

    private String getScheduleUtcOffset() {
        return scheduleUtcOffset;
    }

    public int getRadomisedDeliveryWindow() {
        return radomisedDeliveryWindow;
    }

    public void setRadomisedDeliveryWindow(int radomisedDeliveryWindow) {
        this.radomisedDeliveryWindow = radomisedDeliveryWindow;
    }

    public int getScheduleStartTime() {
        return scheduleStartTime;
    }

    public void setScheduleStartTime(int scheduleStartTime) {
        this.scheduleStartTime = scheduleStartTime;
    }


    public void setScheduleUtcOffset(String scheduleUtcOffset) {
        this.scheduleUtcOffset = scheduleUtcOffset;
    }

    public int getDeliveryFrequency() {
        return deliveryFrequency;
    }

    public void setDeliveryFrequency(int deliveryFrequency) {
        this.deliveryFrequency = deliveryFrequency;
    }



    public int getNumberOfRetries() {
        return numberOfRetries;
    }

    public void setNumberOfRetries(int numberOfRetries) {
        this.numberOfRetries = numberOfRetries;
    }

    public int getRetryPeriod() {
        return retryPeriod;
    }

    public void setRetryPeriod(int retryPeriod) {
        this.retryPeriod = retryPeriod;
    }

    @Override
    public ReadResponse read(ServerIdentity identity, int resourceId) {
        LOG.info("Read on Device Resource " + resourceId);
        switch (resourceId) {
            case SCHEDULE_START_TIME:
                return ReadResponse.success(resourceId, getScheduleStartTime());
            case SCHEDULE_UTC_OFFSET:
                return ReadResponse.success(resourceId, getScheduleUtcOffset());
            case DELIVERY_FREQUENCY:
                return ReadResponse.success(resourceId, getDeliveryFrequency());
            case RANDOMISED_DELIVERY_WINDOW:
                return ReadResponse.success(resourceId, getRadomisedDeliveryWindow());
            case NUMBER_OF_RETRIES:
                return ReadResponse.success(resourceId, getNumberOfRetries());
            case RETRY_PERIOD:
                return ReadResponse.success(resourceId, getRetryPeriod());
            default:
                return super.read(identity, resourceId);
        }
    }


    @Override
    public WriteResponse write(ServerIdentity identity, boolean replace,  int resourceId, LwM2mResource value) {
        LOG.info("Write on Device Resource " + resourceId);
        switch (resourceId) {
            case SCHEDULE_START_TIME:
                setScheduleStartTime(1);
                return WriteResponse.success();
            case SCHEDULE_UTC_OFFSET:
                setScheduleUtcOffset("+10");
                return WriteResponse.success();
            case DELIVERY_FREQUENCY:
                setDeliveryFrequency(1);
                return WriteResponse.success();
            case RANDOMISED_DELIVERY_WINDOW:
                setRadomisedDeliveryWindow(1);
                return WriteResponse.success();
            case NUMBER_OF_RETRIES:
                setNumberOfRetries(1);
                return WriteResponse.success();
            case RETRY_PERIOD:
                setRetryPeriod(1);
                return WriteResponse.success();
            default:
                return super.write(identity, replace, resourceId, value);
        }
    }

}
