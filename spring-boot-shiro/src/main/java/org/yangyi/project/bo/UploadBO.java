package org.yangyi.project.bo;


import java.io.Serializable;

//@ApiModel("附加参数")
public class UploadBO implements Serializable {

    //  校验值
//    @ApiModelProperty("校验值")
    private String checksums;

    //  签名
//    @ApiModelProperty("签名")
    private String signature;

    public String getChecksums() {
        return checksums;
    }

    public void setChecksums(String checksums) {
        this.checksums = checksums;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}
