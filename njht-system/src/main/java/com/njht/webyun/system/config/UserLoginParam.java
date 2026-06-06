package com.njht.webyun.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author David
 * @date 2021/3/2
 * @description：
 */
@Configuration
public class UserLoginParam {

    @Value("${default.password.value:aoto123}")
    private String defaultPassword;
    @Value("${default.password.change.recentTimes:#{5}}")
    private int recentTimes;
    @Value("${default.password.change.recentTimesisEnable:#{false}}")
    private boolean diffWithPrePwd;
    // 是否开启密码强制校验
    @Value("${default.password.change.force:#{false}}")
    private boolean configNeed;
    //修改单位周期：0:year 1:month 2:day 3:hour 4:minute
    @Value("${default.password.change.unit:#{2}}")
    private int unit;
    // 配置了要修改，默认90day
    @Value("${default.password.change.interval:#{90}}")
    private int interval;
    @Value("${default.password.error.lock:#{false}}")
    private boolean errorLock;
    @Value("${default.password.error.times:#{5}}")
    private int errorTimes;
    //是否记录行为日志
    @Value("${default.behaviorlog.enabled:#{true}}")
    private boolean logBehaviorEnabled;
    @Value("${default.decryptParam:#{false}}")
    private boolean decryptParam;
    @Value("${default.mips.enable:#{false}}")
    private boolean mipsEnable;
    @Value("${default.mips.chooseLED:#{true}}")
    private boolean chooseLED;
    @Value("${chooseUnitPaltForm:#{false}}")
    private boolean chooseUnitPaltForm;

    public boolean isMipsEnable() {
        return mipsEnable;
    }

    public void setMipsEnable(boolean mipsEnable) {
        this.mipsEnable = mipsEnable;
    }

    public boolean isChooseLED() {
        return chooseLED;
    }

    public void setChooseLED(boolean chooseLED) {
        this.chooseLED = chooseLED;
    }

    public boolean isDecryptParam() {
        return decryptParam;
    }

    public void setDecryptParam(boolean decryptParam) {
        this.decryptParam = decryptParam;
    }

    public boolean isLogBehaviorEnabled() {
        return logBehaviorEnabled;
    }

    public void setLogBehaviorEnabled(boolean logBehaviorEnabled) {
        this.logBehaviorEnabled = logBehaviorEnabled;
    }

    public boolean isErrorLock() {
        return errorLock;
    }

    public void setErrorLock(boolean errorLock) {
        this.errorLock = errorLock;
    }

    public int getErrorTimes() {
        return errorTimes;
    }

    public void setErrorTimes(int errorTimes) {
        this.errorTimes = errorTimes;
    }

    public String getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    public int getRecentTimes() {
        return recentTimes;
    }

    public void setRecentTimes(int recentTimes) {
        this.recentTimes = recentTimes;
    }

    public boolean isDiffWithPrePwd() {
        return diffWithPrePwd;
    }

    public void setDiffWithPrePwd(boolean diffWithPrePwd) {
        this.diffWithPrePwd = diffWithPrePwd;
    }

    public boolean isConfigNeed() {
        return configNeed;
    }

    public boolean isChooseUnitPaltForm(){
        return chooseUnitPaltForm;
    }

    public void setConfigNeed(boolean configNeed) {
        this.configNeed = configNeed;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
