package org.pg6100.quizApi.dto.hal;

import io.swagger.annotations.ApiModelProperty;

public class HalObject {

    @ApiModelProperty("HAL links")
    public HalLinkSet _links;
}
