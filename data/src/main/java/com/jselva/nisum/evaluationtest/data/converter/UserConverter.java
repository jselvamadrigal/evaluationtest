package com.jselva.nisum.evaluationtest.data.converter;

import com.jselva.nisum.evaluationtest.data.dto.PhoneDto;
import com.jselva.nisum.evaluationtest.data.dto.RoleDto;
import com.jselva.nisum.evaluationtest.data.dto.UserDto;
import com.jselva.nisum.evaluationtest.data.entity.Phone;
import com.jselva.nisum.evaluationtest.data.entity.User;
import com.jselva.nisum.evaluationtest.data.models.RoleType;
import com.jselva.nisum.evaluationtest.data.util.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class UserConverter extends Converter<UserDto, User> {

    public UserConverter() {
        super(UserConverter::convertToEntity, UserConverter::convertToDto);
    }

    private static UserDto convertToDto(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setToken(user.getToken());
        userDto.setActive(user.getActive());
        userDto.setCreated(user.getCreated()
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        userDto.setModified(user.getModified()
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        userDto.setLastLogin(user.getLastLogin()
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime());

        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(RoleType.ROLE_ADMIN.name()))) {
            userDto.setPhones(user.getPhones().stream().map(
                    phone -> new PhoneDto(
                            phone.getNumber(),
                            phone.getCityCode(),
                            phone.getCountryCode()
                    )
            ).collect(Collectors.toList()));

            userDto.setRoles(user.getRoles().stream().map(
                    role -> new RoleDto(
                            role.getName(),
                            role.getDescription()
                    )
            ).collect(Collectors.toList()));
        }

        return userDto;
    }

    private static User convertToEntity(UserDto dto) {
        final User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setToken(dto.getToken());
        user.setActive(true);
        RoleConverter converter = new RoleConverter();
        user.setRoles(new HashSet<>(converter.convertFromDtos(dto.getRoles())));

        List<Phone> phones = dto.getPhones().stream().map(phoneDto -> {
            Phone phone = new Phone();
            phone.setNumber(phoneDto.getNumber());
            phone.setCityCode(phoneDto.getCityCode());
            phone.setCountryCode(phoneDto.getCountryCode());
            phone.setUser(user);
            return phone;
        }).collect(Collectors.toList());

        user.setPhones(new HashSet<>(phones));

        return user;
    }

}
