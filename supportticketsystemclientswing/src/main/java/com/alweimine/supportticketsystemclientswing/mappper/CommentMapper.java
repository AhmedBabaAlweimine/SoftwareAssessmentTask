package com.alweimine.supportticketsystemclientswing.mappper;

import com.alweimine.supportticketsystemclientswing.entities.Comment;
import com.alweimine.supportticketsystemclientswing.mappper.dto.CommentDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class CommentMapper extends AbstractMapper<Comment, CommentDto> {

    private final UserMapper userMapper;

    @Override
    public Comment dtoToEntity(CommentDto dto) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setCreationDate(LocalDateTime.now());
        return comment;
    }

    @Override
    public CommentDto entityToDto(Comment entity) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentId(entity.getCommentId());
        commentDto.setText(entity.getText());
        commentDto.setCreationDate(entity.getCreationDate());
        commentDto.setUserDto(userMapper.entityToDto(entity.getUser()));
        return commentDto;
    }
}
