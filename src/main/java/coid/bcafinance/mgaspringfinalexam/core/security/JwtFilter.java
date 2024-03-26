package coid.bcafinance.mgaspringfinalexam.core.security;

import coid.bcafinance.mgaspringfinalexam.configuration.MyHttpServletRequestWrapper;
import coid.bcafinance.mgaspringfinalexam.configuration.OtherConfig;
import coid.bcafinance.mgaspringfinalexam.core.Crypto;
import coid.bcafinance.mgaspringfinalexam.handler.RequestCapture;
import coid.bcafinance.mgaspringfinalexam.service.UserService;
import coid.bcafinance.mgaspringfinalexam.util.LoggingFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private UserService userService;

    private String [] strExceptionArr = new String[2];
    public JwtFilter() {
        strExceptionArr[0] = "JwtFilter";
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");//ambil header Authorization
        authorization = authorization == null ? "": authorization;
        String token = null;
        String userName = null;
        try{

            /**
             validasi khusus memilah request dengan content type application/json
             */
            String strContentType = request.getContentType()==null?"":request.getContentType();
            if(!strContentType.startsWith("multipart/form-data") || "".equals(strContentType)){
                request = new MyHttpServletRequestWrapper(request);
            }

            /**
             Langkah pertama otentikasi token
             */
            if(!"".equals(authorization) && authorization.startsWith("Bearer ") && authorization.length()>7)
            {
                token = authorization.substring(7);//memotong setelah kata Bearer+spasi = 7 digit

                /**
                 *  DECRYPT TOKEN DARI FRONT END
                 */
                token = Crypto.performDecrypt(token);
                userName = jwtUtility.getUsernameFromToken(token);
                if(userName != null &&
                        SecurityContextHolder.getContext().getAuthentication()==null)
                {
                    if(jwtUtility.validateToken(token))
                    {

                        /**
                         Disini dicek ulang token ke table user apakah valid atau tidak user tersebut.
                         Karena payload di JWT base64 , orang dapat merangkai nya secara manual
                         jadi kalau pilihan nya security maka perlu diverifikasi lagi ke database informasi yang ada di JWT itu valid atau tidak
                         secara performance menurun karena ada step harus membuka koneksi ke database
                         akan tetapi lebih aman kalau divalidasi 2 kali...
                         */
                        final UserDetails userDetails = userService.loadUserByUsername(userName);
                        final UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null,
                                        userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            strExceptionArr[1] = "oFilterInternal(HttpServletRequest request, HttpServletResponse response,FilterChain filterChain) "+ RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, ex, OtherConfig.getFlagLoging());
        }

        filterChain.doFilter(request,response);
    }
}