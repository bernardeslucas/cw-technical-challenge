package br.com.cwi.technicalchallenge.controller.v1.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
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
