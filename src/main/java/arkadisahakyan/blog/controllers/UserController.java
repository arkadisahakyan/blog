package arkadisahakyan.blog.controllers;

import arkadisahakyan.blog.dto.ArticleSummaryDTO;
import arkadisahakyan.blog.dto.PaginationDTO;
import arkadisahakyan.blog.services.ArticleManagementService;
import arkadisahakyan.blog.services.IArticleManagementService;
import arkadisahakyan.blog.services.IUserManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private final IUserManagementService userManagementService;
    private final IArticleManagementService articleManagementService;

    @Autowired
    public UserController(IUserManagementService userManagementService, IArticleManagementService articleManagementService) {
        this.userManagementService = userManagementService;
        this.articleManagementService = articleManagementService;
    }

    @GetMapping
    public String user(Model model, @RequestParam(defaultValue = "1") Integer page) {
        Page<ArticleSummaryDTO> articles = articleManagementService.getAllArticleSummariesOfCurrentUser(PageRequest.of( page - 1, 10));
        model.addAttribute("articles", articles.toList());
        model.addAttribute("pagination", new PaginationDTO(page, articles.getTotalPages(), ArticleManagementService.DEFAULT_PAGINATION_SIZE, 10));
        return "user_page";
    }

    @PreAuthorize("hasAnyAuthority(\"ADMIN\")")
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String manageUser(@RequestBody MultiValueMap<String, String> formData, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        return userManagementService.manageUser(formData, request, redirectAttributes);
    }
}
