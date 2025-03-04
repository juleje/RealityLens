package be.kdg.backendjava.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikesCommentsDto {
    private int numberOfLikes;
    private int numberOfComments;
    private boolean haveLiked;
    private boolean amIloggedIn;



}
