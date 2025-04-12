package api.game_event_notifier.service.user;

import api.game_event_notifier.model.entity.SysUser;
import api.game_event_notifier.model.request.LoginRequestModel;
import api.game_event_notifier.repository.SysUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class CreateUserService {

    private final SysUserRepository _userRepository;

    public CreateUserService(SysUserRepository userRepository) {
        this._userRepository = userRepository;
    }

    public SysUser createUser(LoginRequestModel newUser) {
        try
        {
            var existUser = _userRepository.findByUsername(newUser.getUsername());
            if (existUser == null)
            {
                String hashedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());
                var user = new SysUser();
                user.setUsername(newUser.getUsername());
                user.setPassword(hashedPassword);
                user.setSalt(generateSalt());
                user.setCreateDate(LocalDateTime.now());
                user.setUpdateDate(LocalDateTime.now());
                return _userRepository.save(user);
            }
            else
            {
                return existUser;
            }
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    private String generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        int SALT_LENGTH = 32;
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);

        // แปลง Salt ให้เป็น Base64 เพื่อให้เป็น String ที่สามารถใช้งานได้
        return Base64.getEncoder().encodeToString(salt);
    }
}