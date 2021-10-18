package jwt.example.jwt.controler;

import jwt.example.jwt.model.JwtRequest;
import jwt.example.jwt.model.JwtResponse;
import jwt.example.jwt.service.UserService;
import jwt.example.jwt.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeControler {
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @GetMapping("/")
    public String home(){
        return "welcome to jwt";
    }
   @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest)throws Exception {
       try {
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           jwtRequest.getUsername(),
                           jwtRequest.getPassword()
                   )
           );
       } catch (BadCredentialsException e) {
           throw new Exception("INVALID CREDENTIALS", e);
       }

       final UserDetails userDetails
               = userService.loadUserByUsername(jwtRequest.getUsername());

       final String token =
              jwtUtility.generateToken(userDetails);

       return  new JwtResponse(token);
   }
}
