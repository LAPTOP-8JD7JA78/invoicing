package com.smartech.invoicingprod.security;

import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider {
//	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
// 
//        String name = authentication.getName();
//        String password = authentication.getCredentials().toString();
//        
//        //Users u = usersService.searchCriteriaUserName(name);
//        if(u != null) {
//        	if(u.isEnabled()) {
//        		    String encodePass = Base64.getEncoder().encodeToString(password.trim().getBytes());
//        		    String pass = u.getPassword();
//        		    pass = pass.replace("==a20$", "");
//        		    if(encodePass.equals(pass)) {
//        		    	String userRole = u.getSystemRole();
//                        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//                        authorities.add(new SimpleGrantedAuthority(userRole));
//                        
//                        return new UsernamePasswordAuthenticationToken(name, password, authorities);
//        		    }else {        		    		
//        		            throw new BadCredentialsException("La contraseña es incorrecta");
//        		    }
//        	}else {
//        		throw new DisabledException("El usuario se encuentra deshabilitado");
//        	}
//
//        }else {
//        	throw new AuthenticationCredentialsNotFoundException("Las credenciales ingresadas (usuario y contraseña) son inválidas");
//        }
//    }
//
//	public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
}
