package com.example.web;

import com.example.domain.model.Contact;
import com.example.domain.model.GridContact;
import com.example.domain.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

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

    @PreAuthorize("hasRole('UPDATE')")
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
    public String update(@Valid Contact contact, BindingResult bindingResult, Model model,
                         HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "zalupa");
            model.addAttribute("contact", contact);
            return "contacts/update";
        }
        model.asMap().clear();
        contact.setPhoto(Optional.ofNullable(file).map(f -> {
            try {
                return f.getBytes();
            } catch (IOException e) {
                return null;
            }
        }).orElse(null));
        contactRepository.save(contact);
        return "redirect:/contacts/" + UriUtils.encodePathSegment(contact.getId().toString(), httpServletRequest.getCharacterEncoding());
    }

    @PreAuthorize("hasRole('ADD')")
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

    @ResponseBody
    @RequestMapping(value = "/listgrid", method = RequestMethod.GET, produces = "application/json")
    public GridContact<Contact> listGrid(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows,
            @RequestParam(value = "sidx", required = false, defaultValue = "firstName") String sortBy,
            @RequestParam(value = "sord", required = false, defaultValue = "asc") String order
    ) {
        Page<Contact> contacts = contactRepository
                .findAll(new PageRequest(page - 1, rows, Sort.Direction.fromString(order), sortBy));

        GridContact<Contact> result = new GridContact<>();
        result.setCurrentPage(contacts.getNumber() + 1);
        result.setTotalPages(contacts.getTotalPages());
        result.setTotalRecords(contacts.getTotalElements());
        result.setData(contacts.getContent());
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/photo/{id}", method = RequestMethod.GET)
    public byte[] downloadPhoto(@PathVariable("id") Long id) {
        Contact contact = contactRepository.findOne(id);
        return contact.getPhoto();
    }
}
