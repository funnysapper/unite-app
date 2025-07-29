package com.Unite.UniteMobileApp.utils;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.Phonenumber;

    public class PhoneNumberValidator {

        private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

        public static boolean isValid(String phoneNumber, String regionCode) {
            try {
                Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNumber, regionCode);
                return phoneUtil.isValidNumber(number);
            } catch (NumberParseException e) {
                return false;
            }
        }
    }


