package br.com.cwi.technicalchallenge.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VotingSessionRequest {

    @ApiModelProperty(value= "If not informed, the duration of the Voting Session will be set to 1 minute.", example = "60")
    private Integer durationMinutes;

}

