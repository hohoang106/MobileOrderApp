package com.sh.orderapp.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodCommentModel implements Serializable {

    private String id;
    private String content;
    private String createdDate;
    private FoodModel foodModel;
    private UserModel userCommentModel;

}
