package org.pg6100.quizApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.pg6100.quizApi.dto.hal.HalLink;
import org.pg6100.quizApi.dto.hal.HalLinkSet;
import org.pg6100.quizApi.dto.hal.HalObject;

import java.util.List;

@ApiModel(description = "Paginated list of resources with HAL links ")
public class ListDto<T> extends HalObject {

    @ApiModelProperty("The list of resources in the current retrieved page")
    public List<T> list;

    @ApiModelProperty("The index of first element in this page")
    public Integer rangeMin;

    @ApiModelProperty("The index of the last element of this page")
    public Integer rangeMax;

    @ApiModelProperty("The total number of elements in all pages")
    public Integer totalSize;

    @ApiModelProperty("HAL links")
    public ListLinks _links;


    public static class ListLinks extends HalLinkSet {

        @ApiModelProperty("Link to the 'next' page")
        public HalLink next;

        @ApiModelProperty("Link to the 'previous' page")
        public HalLink previous;
    }
}