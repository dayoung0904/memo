package com.memo.post.bo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;

@Service
public class PostBO {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private FileManagerService fileManager;
	
	// input:userId		output:List<Post>
	public List<Post> getPostListByUserId(int userId){
		return postMapper.selectPostListByUserId(userId);
	}
	
	// input: postId, userId		output:Post or null
	public Post getPostBypostIdUserId(int postId, int userId) {
		return postMapper.selectPostBypostIdUserId(postId, userId);
	}
	
	// input:파라미터들 		output:X
	public void addPost(int userId, String userLoginId, String subject, String content, MultipartFile file) {
		String imagePath = null;
		
		// 이미지가 있으면 업로드 새로운 클래스를 만들어서 공통적으로 사용할 수 있도록 만들 것!
		if(file != null) {
			imagePath = fileManager.saveFile(userLoginId, file);
		}
		
		postMapper.insertPost(userId, subject, content, imagePath);
	}
	
	// input:파라미터들		output:X
	public void updatePost(int userId, String userLoginId, 
			int postId, String subject, String content, 
			MultipartFile file) {
		
		// 기존 글을 가져와본다. (1. 이미지 교체 시 삭제를 위해서	2. 업데이트 대상이 있는지 확인)
		Post post = postMapper.selectPostBypostIdUserId(postId, userId);
		if(post == null) {
			logger.error("[글 수정] post is null. postId:{}, userId:{}", postId, userId); // 나중을 위한 단서
			return;
		}
		
		// 파일이 있다면
		// 1) 새 이미지를 업로드 한다.
		// 2) 새 이미지 업로드 성공 시 기존 이미지 제거(기존 이미지가 있을 때)
		String imagePath = null;
		if(file != null) {
			// 업로드
			imagePath = fileManager.saveFile(userLoginId, file);
			
			// 업로드 성공 시 기존 이미지 제거(있으면)
			if(imagePath != null && post.getImagePath() != null) {
				// 업로드가 성공했고, 기존 이미지가 존재한다면 => 삭제 
				// 이미지 제거
				fileManager.deleteFile(post.getImagePath());
			}
		}
		
		// DB 글 update
		postMapper.updatePostByPostIdUserId(postId, userId, subject, content, imagePath);
	}
	
	// input:글 번호, 글쓴이 번호		output:X
	public void deletePostByPostIdUserId(int postId, int userId) {
		// 기존글 가져옴(이미지가 있으면 삭제해야 하기 때문)
		Post post = postMapper.selectPostBypostIdUserId(postId, userId);
		if(post == null) {
			logger.info("[글 삭제] post가 null. postId:{}, userId:{}", postId, userId);
			return;
		}
		// 기존 이미지가 존재한다면 -> 삭제
		if(post.getImagePath() != null) {
			fileManager.deleteFile(post.getImagePath());
		}
		// db 삭제
		postMapper.deletePostByPostIdUserId(postId, userId);
	}
}
