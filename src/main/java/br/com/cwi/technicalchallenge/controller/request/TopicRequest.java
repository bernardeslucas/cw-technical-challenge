package br.com.cwi.technicalchallenge.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TopicRequest {

    @ApiModelProperty(example = "The company is suggesting a new supplier called 'OtherCompany' to become our main supplier.", required = true)
    private String description;
    @ApiModelProperty(example = "New supplier voting", required = true)
    private String title;

}