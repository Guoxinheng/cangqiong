package com.mapper;


import com.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;


@Mapper
public interface AddressBookMapper {
    void addAddress(AddressBook addressBook);

   @Select("select * from address_book where user_id = #{userId} and is_default = 1")
    AddressBook getAddressDefault(Long userId);
   @Select("select * from address_book where user_id = #{userId}")
    List<AddressBook> getList(Long userId);
   @Update("update address_book set is_default = #{isDefault} where id = #{id}")
    void updateAddressDefault(AddressBook addressBook);
   @Update("update address_book set is_default = 1 where id=#{id}")
    void updateAddressDefaultById(Long id);
   @Select("select * from address_book where id = #{id} ")
    AddressBook getAddressById(Long id);
    void updateAddress(AddressBook addressBook);
    @Delete("delete from address_book where id = #{id}")
    void deleteAddress(Long id);
}
