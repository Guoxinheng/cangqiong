package com.service;

import com.constant.dto.AddressBookDTO;
import com.entity.AddressBook;
import com.vo.AddressBookVO;

import java.util.List;


public interface AddressBookService {
    void addAddress(AddressBookDTO addressBookDTO);

    List<AddressBook> list();

    AddressBookVO getDefault();

    void setDefault(Long id);

    AddressBook getAddressById(Long id);

    void updateAddress(AddressBook addressBook);

    void deleteAddress(Long id);
}
