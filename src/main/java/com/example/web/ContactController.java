package com.example.web;

import com.example.domain.model.Contact;
import com.example.domain.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by grisha on 25.02.17.
 */
@RequestMapping("/contacts")
@Controller
public class ContactController {

    @Autowired
    ContactRepository contactRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String welcome(Model model) {
        List<Contact> contacts = contactRepository.findAll();
        model.addAttribute("contacts", contacts);
        return "contacts/list";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model model) {
        Contact contact = contactRepository.findOne(id);
        model.addAttribute("contact", contact);
        return "contacts/show";
    }

    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        Contact contact = contactRepository.findOne(id);
        model.addAttribute("contact", contact);
        return "contacts/update";
    }

    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String newForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "contacts/update";
    }

    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
    public String update(@Valid Contact contact, BindingResult bindingResult, Model model,
                         HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "zalupa");
            model.addAttribute("contact", contact);
            return "contacts/update";
        }
        model.asMap().clear();
        contactRepository.save(contact);
        return "redirect:/contacts/" + UriUtils.encodePathSegment(contact.getId().toString(), httpServletRequest.getCharacterEncoding());
    }

    @RequestMapping(params = "form", method = RequestMethod.POST)
    public String create(@Valid Contact contact, BindingResult bindingResult, Model model,
                         HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "zalupa");
            model.addAttribute("contact", contact);
            return "contacts/update";
        }
        model.asMap().clear();
        contactRepository.save(contact);
        return "redirect:/contacts/" + UriUtils.encodePathSegment(contact.getId().toString(), httpServletRequest.getCharacterEncoding());
    }

}
