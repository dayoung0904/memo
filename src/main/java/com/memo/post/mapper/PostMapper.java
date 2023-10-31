package com.memo.post.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.memo.post.domain.Post;

@Repository
public interface PostMapper {

	public List<Map<String, Object>> selectPostList();
	
	public List<Post> selectPostListByUserId(int userId);
}
