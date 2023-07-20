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
import org.eclipse.leshan.core.request.argument.Arguments;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;
import org.eclipse.leshan.core.util.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OMA10281LowBatteryAlarm extends BaseInstanceEnabler {
    private static final Logger LOG = LoggerFactory.getLogger(OMA10281LowBatteryAlarm.class);
    private final ScheduledExecutorService scheduler;

    private static final int EVENT_TYPE = 6011; //RW
    private static final int ALARM_REALTIME = 6012; //RW
    private static final int ALARM_STATE = 6013; //R
    private static final int ALARM_SET_THRESHOLD = 6014; //RW
    private static final int ALARM_SET_OPERATOR = 6015; //Rw
    private static final int ALARM_CLEAR_THRESHOLD = 6016; //RW
    private static final int ALARM_CLEAR_OPERATOR = 6017; //RW
    private static final int ALARM_MAXIMUM_EVENT_COUNT = 6018; //RW
    private static final int ALARM_MAXIMUM_EVENT_PERIOD = 6019; //RW
    private static final int LATEST_DELIVERY_EVENT_TIME = 6020; //RW
    private static final int LATEST_RECORDED_EVENT_TIME = 6021; //R
    private static final int ALARM_CLEAR= 6022; //E
    private static final int ALARM_AUTO_CLEAR = 6023; //RW
    private static final int EVENT_CODE = 6024; //R
    private static final int LATEST_PAYLOAD = 6025; //R

    private int eventType;
    private boolean alarmRealtime;
    private boolean alarmState;
    private float alarmSetThreshold;
    private int alarmSetOperator;
    private float alarmClearThreshold;
    private int alarmClearOperator;
    private int alarmMaximumEventCount;
    private int alarmMaximumEventPeriod;
    private Date latestDeliveryEventTime = new Date();
    private Date latestRecordedEventTime = new Date();
    private boolean alarmAutoClear;
    private byte[] latestPayload = "8319281a01831a5a983fa0193840861903f31901c50c18391908ae190566".getBytes();
    private int eventCode;


    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public boolean isAlarmRealtime() {
        return alarmRealtime;
    }

    public void setAlarmRealtime(boolean alarmRealtime) {
        this.alarmRealtime = alarmRealtime;
    }

    public boolean isAlarmState() {
        return alarmState;
    }

    public void setAlarmState(boolean alarmState) {
        this.alarmState = alarmState;
    }

    public float getAlarmSetThreshold() {
        return alarmSetThreshold;
    }

    public void setAlarmSetThreshold(float alarmSetThreshold) {
        this.alarmSetThreshold = alarmSetThreshold;
    }

    public int getAlarmSetOperator() {
        return alarmSetOperator;
    }

    public void setAlarmSetOperator(int alarmSetOperator) {
        this.alarmSetOperator = alarmSetOperator;
    }

    public float getAlarmClearThreshold() {
        return alarmClearThreshold;
    }

    public void setAlarmClearThreshold(float alarmClearThreshold) {
        this.alarmClearThreshold = alarmClearThreshold;
    }

    public int getAlarmClearOperator() {
        return alarmClearOperator;
    }

    public void setAlarmClearOperator(int alarmClearOperator) {
        this.alarmClearOperator = alarmClearOperator;
    }

    public int getAlarmMaximumEventCount() {
        return alarmMaximumEventCount;
    }

    public void setAlarmMaximumEventCount(int alarmMaximumEventCount) {
        this.alarmMaximumEventCount = alarmMaximumEventCount;
    }

    public int getAlarmMaximumEventPeriod() {
        return alarmMaximumEventPeriod;
    }

    public void setAlarmMaximumEventPeriod(int alarmMaximumEventPeriod) {
        this.alarmMaximumEventPeriod = alarmMaximumEventPeriod;
    }

    public Date getLatestDeliveryEventTime() {
        return latestDeliveryEventTime;
    }

    public void setLatestDeliveryEventTime(Date latestDeliveryEventTime) {
        this.latestDeliveryEventTime = latestDeliveryEventTime;
    }

    public Date getLatestRecordedEventTime() {
        return latestRecordedEventTime;
    }

    public void setLatestRecordedEventTime(Date latestRecordedEventTime) {
        this.latestRecordedEventTime = latestRecordedEventTime;
    }

    public boolean isAlarmAutoClear() {
        return alarmAutoClear;
    }

    public void setAlarmAutoClear(boolean alarmAutoClear) {
        this.alarmAutoClear = alarmAutoClear;
    }

    public byte[] getLatestPayload() {
        return latestPayload;
    }

    public void setLatestPayload(byte[] latestPayload) {
        this.latestPayload = latestPayload;
    }

    public double getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(double currentTemp) {
        this.currentTemp = currentTemp;
    }

    @Override
    public ReadResponse read(ServerIdentity identity, int resourceId) {
        LOG.info("Read on Device Resource " + resourceId);
        switch (resourceId) {
            case EVENT_TYPE:
                return ReadResponse.success(resourceId, getEventType());
            case ALARM_REALTIME:
                return ReadResponse.success(resourceId, isAlarmRealtime());
            case ALARM_STATE:
                return ReadResponse.success(resourceId, isAlarmState());
            case ALARM_SET_THRESHOLD:
                return ReadResponse.success(resourceId, getAlarmSetThreshold());
            case ALARM_SET_OPERATOR:
                return ReadResponse.success(resourceId, getAlarmSetOperator());
            case ALARM_CLEAR_THRESHOLD:
                return ReadResponse.success(resourceId, getAlarmClearThreshold());
            case ALARM_CLEAR_OPERATOR:
                return ReadResponse.success(resourceId, getAlarmClearOperator());
            case ALARM_MAXIMUM_EVENT_COUNT:
                return ReadResponse.success(resourceId, getAlarmMaximumEventCount());
            case ALARM_MAXIMUM_EVENT_PERIOD:
                return ReadResponse.success(resourceId, getAlarmMaximumEventPeriod());
            case LATEST_DELIVERY_EVENT_TIME:
                return ReadResponse.success(resourceId, getLatestDeliveryEventTime());
          case ALARM_AUTO_CLEAR:
                return ReadResponse.success(resourceId, isAlarmAutoClear());
            case EVENT_CODE:
                return ReadResponse.success(resourceId, getEventCode());
            case LATEST_PAYLOAD:
                return ReadResponse.success(resourceId, getLatestPayload());
            case LATEST_RECORDED_EVENT_TIME:
                return ReadResponse.success(resourceId, getLatestRecordedEventTime());
            default:
                return super.read(identity, resourceId);
        }
    }


    @Override
    public WriteResponse write(ServerIdentity identity, boolean replace, int resourceId, LwM2mResource value) {
        LOG.info("Write on Device Resource " + resourceId);
        switch (resourceId) {
            case EVENT_TYPE:
                setEventType(1);
                return WriteResponse.success();
            case ALARM_REALTIME:
                setAlarmRealtime(true);
                return WriteResponse.success();
            case ALARM_SET_THRESHOLD:
                setAlarmSetThreshold(1);
                return WriteResponse.success();
            case ALARM_SET_OPERATOR:
                setAlarmSetOperator(1);
                return WriteResponse.success();
            case ALARM_CLEAR_THRESHOLD:
                setAlarmClearThreshold(1);
                return WriteResponse.success();
            case ALARM_CLEAR_OPERATOR:
                setAlarmClearOperator(1);
                return WriteResponse.success();
            case ALARM_MAXIMUM_EVENT_COUNT:
                setAlarmMaximumEventCount(1);
                return WriteResponse.success();
            case ALARM_MAXIMUM_EVENT_PERIOD:
                setAlarmMaximumEventPeriod(1);
                return WriteResponse.success();
            case LATEST_DELIVERY_EVENT_TIME:
                setLatestDeliveryEventTime(new Date());
                return WriteResponse.success();
            case ALARM_AUTO_CLEAR:
                setAlarmAutoClear(true);
                return WriteResponse.success();
            case LATEST_RECORDED_EVENT_TIME:
                setLatestRecordedEventTime(new Date());
                return WriteResponse.success();
            default:
                return super.write(identity, replace, resourceId, value);
        }
    }


    @Override
    public ExecuteResponse execute(ServerIdentity identity, int resourceid, Arguments params) {
        LOG.info("Execute on Device resource " + resourceid);
          switch (resourceid) {
              case ALARM_CLEAR:
                LOG.info("Alarm Clear" + resourceid);
                return ExecuteResponse.success();

            default:
                return super.execute(identity, resourceid, params);
        }
    }

    public OMA10281LowBatteryAlarm () {
        this.scheduler = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("OMA10281LowBatteryAlarm"));
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
        fireResourceInstanceChange(LATEST_PAYLOAD, 2);
    }
}
