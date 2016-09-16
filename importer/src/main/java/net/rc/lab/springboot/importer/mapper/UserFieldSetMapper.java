package net.rc.lab.springboot.importer.mapper;

import net.rc.lab.springboot.core.repository.CompanyRepository;
import net.rc.lab.springboot.core.repository.RoleRepository;
import net.rc.lab.springboot.entities.CompanyVo;
import net.rc.lab.springboot.entities.RoleVo;
import net.rc.lab.springboot.entities.UserVo;

import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.util.Optional;

@Component
public class UserFieldSetMapper extends AbstractFieldSetMapper<UserVo> {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private CompanyRepository companyRepository;

  @Override
  public UserVo mapFieldSet(FieldSet fieldSet) throws BindException {
    UserVo user = new UserVo();
    user.setLoginId(fieldSet.readString("Id"));

    String password = fieldSet.readString("Password");
    String salt = BCrypt.gensalt();
    String hashSalt = BCrypt.hashpw(password, salt);

    user.setPasswordSalt(salt);
    user.setPasswordHash(hashSalt);

    String role = fieldSet.readString("Role");
    Optional<RoleVo> roleVo = roleRepository.findByName(role);
    if (roleVo.isPresent()) {
      user.setRole(roleVo.get());
    }

    String company = fieldSet.readString("Company");
    Optional<CompanyVo> companyVo = companyRepository.findByNameEn(company);
    if (companyVo.isPresent()) {
      user.setCompany(companyVo.get());  
    }

    user.setEmail(fieldSet.readString("Email"));
    user.setPhoneNo(fieldSet.readString("Phone No"));
    user.setMobileNo(fieldSet.readString("Mobile No"));
    return user;
  }

}