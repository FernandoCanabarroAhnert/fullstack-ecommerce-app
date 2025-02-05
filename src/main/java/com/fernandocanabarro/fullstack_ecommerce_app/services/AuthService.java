package com.fernandocanabarro.fullstack_ecommerce_app.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ActivateAccountDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.EmailDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.PasswordRecoverDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.RegistrationRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.RoleDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserNewPasswordDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserUpdateDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserUpdateInfoDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;

public interface AuthService {

    void register(RegistrationRequestDTO dto);

    void sendConfirmationEmail(User user);

    String createActivationCodeAndSave(User user);

    String createCode();

    void activateAccount(ActivateAccountDTO dto);

    void createPasswordRecover(EmailDTO dto);

    void saveNewPassword(PasswordRecoverDTO dto);

    List<UserResponseDTO> adminFindAll();
    Page<UserResponseDTO> adminFindAllUsersPaginated(String name,Pageable pageable);

    void adminCreateUser(UserRequestDTO dto);

    void adminUpdateUser(Long id, UserUpdateDTO dto);

    List<RoleDTO> findAllRoles();

    UserUpdateDTO findByIdToUpdate(Long id);

    void deleteUser(Long id);

    User getConnectedUser();

    UserResponseDTO getConnectedUserDTO();

    void userUpdatePassword(UserNewPasswordDTO dto);

    void userUpdateInfo(UserUpdateInfoDTO dto);
}
