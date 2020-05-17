package br.com.cwi.technicalchallenge.controller.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TopicResponse {

    private String id;
    private String title;
    private String description;
    private String result;

}
