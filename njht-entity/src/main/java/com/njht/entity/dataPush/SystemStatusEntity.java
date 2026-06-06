package com.njht.entity.dataPush;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author daiguojun
 * @date 2022-09-06 14:58
 * 系统运行状态实体
 */
@Data
@NoArgsConstructor
public class SystemStatusEntity extends PushDataCommonEntity {

    private static final long serialVersionUID = 4228973491113457277L;
    /**
     * 服务器IP
     */
    @JsonProperty("server_ip")
    private String serverIp;

    /**
     * 服务类型
     */
    @JsonProperty("server_type")
    private String serverType;

    /**
     * 服务状态
     */
    @JsonProperty("server_status")
    private String serverStatus;

    public SystemStatusEntity(String serverIp, String serverType, String serverStatus) {
        this.serverIp = serverIp;
        this.serverType = serverType;
        this.serverStatus = serverStatus;
    }

    public SystemStatusEntity(String serverType) {
        this.serverType = serverType;
    }
}
