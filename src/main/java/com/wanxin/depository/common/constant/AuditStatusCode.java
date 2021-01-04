package com.wanxin.depository.common.constant;

/**
 * <P>
 * 审核状态
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
public enum AuditStatusCode {

    AUDIT("AUDIT", "审核中"),
    PASSED("PASSED", "审核通过"),
    REFUSED("REFUSED", "审核拒绝");

    private String code;
    private String desc;

    AuditStatusCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
