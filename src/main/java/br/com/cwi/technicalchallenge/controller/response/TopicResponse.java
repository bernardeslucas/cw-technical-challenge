package br.com.cwi.technicalchallenge.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
public class TopicResponse {

    @ApiModelProperty(example = "1")
    private long id;
    @ApiModelProperty(example = "New supplier voting")
    private String title;
    @ApiModelProperty(example = "The company is suggesting a new supplier called 'OtherCompany' to become our main supplier.")
    private String description;
    @ApiModelProperty(example = "Running votation -> YES votes: [5], NO votes: [3].")
    private String result;

}
