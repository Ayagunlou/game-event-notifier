package api.game_event_notifier.service.user;

import api.game_event_notifier.model.entity.SysUser;
import api.game_event_notifier.model.reponse.LoginResponseModel;
import api.game_event_notifier.model.request.LoginRequestModel;
import api.game_event_notifier.repository.SysUserRepository;
import api.game_event_notifier.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginUserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final SysUserRepository sysUserRepository;


    public LoginUserService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, SysUserRepository sysUserRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.sysUserRepository = sysUserRepository;
    }

    public LoginResponseModel login(LoginRequestModel newUser) {
        try
        {
            SysUser user = sysUserRepository.findByUsername(newUser.getUsername());

            if (user == null) {
                // 👇 custom error return
                return new LoginResponseModel("","","User not found");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            newUser.getUsername(),
                            newUser.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);
            return new LoginResponseModel(newUser.getUsername(),jwt,"success");
        }
        catch (BadCredentialsException e)
        {
            return new LoginResponseModel("","","Invalid password");
        }
    }
}