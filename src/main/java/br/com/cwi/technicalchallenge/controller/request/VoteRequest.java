package br.com.cwi.technicalchallenge.controller.request;

import br.com.cwi.technicalchallenge.domain.VoteOption;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class VoteRequest {

    @ApiModelProperty(value = "IDs in the database start in 1.", example = "1", required = true)
    private long idAssociate;
    @ApiModelProperty(example = "YES", required = true)
    private VoteOption voteOption;

}
