package org.pg6100.gameApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiModel("Keeps track of all games")
public class GameDto {

    @ApiModelProperty("The unique id that identifies this game")
    public Long id;

    @ApiModelProperty("Questions of the quiz and if your answer was correct")
    public Map<QuestionDto, Boolean> questions;

    public GameDto(){}

    public GameDto(Long id, List<QuestionDto> questions){
        this.id = id;
        this.questions = new HashMap<>();
        questions.addAll(questions);
    }
}
