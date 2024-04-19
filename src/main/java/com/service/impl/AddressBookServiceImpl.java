package com.service.impl;

import com.constant.MessageConstant;
import com.context.BaseContext;
import com.constant.dto.AddressBookDTO;
import com.entity.AddressBook;
import com.exception.AddressDeleteErrorException;
import com.mapper.AddressBookMapper;
import com.service.AddressBookService;
import com.vo.AddressBookVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;
    //添加用户地址


    /**
     * @return void
     * @param addressBookDTO
     */
    @Override
    public void addAddress(AddressBookDTO addressBookDTO) {
        //如果是新增的第一个地址则设置为默认地址
        //如果不是新增的地址则不设置为默认地址
        //先查询数据库看看是否有该用户的地址
        Long userId=BaseContext.getCurrentId();
        List<AddressBook> list=new ArrayList<>();
        list=addressBookMapper.getList(userId);
          if (list.size()>0)
          {
              AddressBook addressBook=new AddressBook() ;
              BeanUtils.copyProperties(addressBookDTO,addressBook);
              addressBook.setUserId(BaseContext.getCurrentId());
              addressBook.setIsDefault(0);
              addressBookMapper.addAddress(addressBook);
          }
          else
          {
              AddressBook addressBook=new AddressBook() ;
              BeanUtils.copyProperties(addressBookDTO,addressBook);
              addressBook.setUserId(BaseContext.getCurrentId());
              addressBook.setIsDefault(1);
              addressBookMapper.addAddress(addressBook);
          }

    }

    @Override
    //查询当前用户名下的所有地址
    public List<AddressBook> list() {
        Long userId=BaseContext.getCurrentId();
        List<AddressBook> addressBook=new ArrayList<>();
        addressBook=addressBookMapper.getList(userId);

        return addressBook;
    }

    @Override
    //获取当前用户的默认地址
    public AddressBookVO getDefault() {
        Long userId=BaseContext.getCurrentId();
        AddressBook addressBook=addressBookMapper.getAddressDefault(userId);
        AddressBookVO addressBookVO=new AddressBookVO();
        BeanUtils.copyProperties(addressBook,addressBookVO);
        return addressBookVO;
    }

    @Override
    //设置当前用户的默认地址
    @Transactional
    public void setDefault(Long id) {
        //先查询出来当前用户默认地址
        AddressBook addressBook=addressBookMapper.getAddressDefault(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        //然后修改当前用户地址
        addressBookMapper.updateAddressDefault(addressBook);
        //再把穿过地址改为默认地址
        addressBookMapper.updateAddressDefaultById(id);
    }

    @Override
    //根据id查询当前用户的信息 数据回显
    public AddressBook getAddressById(Long id) {
        AddressBook addressBook=new AddressBook();
        addressBook=addressBookMapper.getAddressById(id);
        return addressBook;
    }

    @Override
    //根据id修改地址
    public void updateAddress(AddressBook addressBook) {
        addressBookMapper.updateAddress(addressBook);
    }

    @Override
    public void deleteAddress(Long id) {
        //如果是默认地址就不可以删除,并抛出异常
        //先查询地址
        AddressBook addressBook=addressBookMapper.getAddressById(id);
        if (addressBook.getIsDefault()==1)
        {
            throw new AddressDeleteErrorException(MessageConstant.User_Address_Delete_Error);
        }
        //如果不是默认地址就可以删除
        addressBookMapper.deleteAddress(id);
    }


}
