package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.SpringConfig;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private static final String API_POST_ID = "/api/posts/\\d+";
    private static final String API_POST = "/api/posts";
    private PostController controller;

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext(SpringConfig.class);

        final var repository = (PostRepository) context.getBean("postRepository");
        final var service = (PostService) context.getBean("postService");
        controller = (PostController) context.getBean("postController");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        final var path = req.getRequestURI();
        // primitive routing
        if (path.equals(API_POST)) {
            controller.all(resp);
            return;
        }
        if (path.matches(API_POST_ID)) {
            // easy way
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
            controller.getById(id, resp);
            return;
        }
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final var path = req.getRequestURI();
        if (path.equals(API_POST)) {
            controller.save(req.getReader(), resp);
            return;
        }
        super.doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final var path = req.getRequestURI();
        if (path.matches(API_POST_ID)) {
            // easy way
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
            controller.removeById(id, resp);
            return;
        }
        super.doDelete(req, resp);
    }
}

