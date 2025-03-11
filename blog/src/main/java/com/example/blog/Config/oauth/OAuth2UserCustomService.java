package com.example.blog.Config.oauth;

import com.example.blog.domain.User;
import com.example.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest); //유저 리퀘스트를 이용해 유저를 읽어 옴
        saveOrUpdate(user); //저장하거나 업데이트
        return user; // 유저 반환
    }

    private void saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes(); //유저 정보를 가져와서 Map 에다가 하는 거 같은데
        String email = (String) attributes.get("email"); //이메일을 가져와?
        String name = (String) attributes.get("name"); //이름도 가져와?
        User user = userRepository.findByEmail(email) //이메일을 사용해 유저를 조회하는 듯?
                .map(entity -> entity.update(name)) //이메일로 유저를 찾았을 때 있으면 업데이트 하고 없으면 만들라고?
                .orElse(User.builder() //아 그렇네 이걸로 새로 만드는 거구나
                        .email(email)
                        .nickname(name)
                        .build());
        userRepository.save(user); //DB에 유저 저장
    }
}
