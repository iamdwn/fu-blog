package tech.fublog.FuBlog.service;

import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.CategoryEntity;
import tech.fublog.FuBlog.exception.BlogPostException;
import tech.fublog.FuBlog.repository.*;

import java.util.*;

@Service
public class AdminService {
    private final CategoryRepository categoryRepository;
    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;
    private final BlogPostReportRepository blogPostReportRepository;
    private final UserReportRepository userReportRepository;

    public AdminService(CategoryRepository categoryRepository, BlogPostRepository blogPostRepository, UserRepository userRepository, BlogPostReportRepository blogPostReportRepository, UserReportRepository userReportRepository) {
        this.categoryRepository = categoryRepository;
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
        this.blogPostReportRepository = blogPostReportRepository;
        this.userReportRepository = userReportRepository;
    }

    public int getCurrentMonth() {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int month = calendar.get(Calendar.MONTH) + 1;
        return month;
    }

    public Long countBlogInMonth() {
        Long countBlog = blogPostRepository.countAllInCurrentMonth();
        return countBlog;
    }

    public Double calculateBlogReportWeight() {
        Double countCurrent = blogPostReportRepository.countAllInCurrentMonth();
        Double countPrevious = getCurrentMonth() == 1
            ? blogPostReportRepository.countAllInPreviousMonthAndYear()
            : blogPostReportRepository.countAllInPreviousMonth();
        return countPrevious != 0 ? (countCurrent / countPrevious) - 1 : 0;
    }

    public Double calculateUserReportWeight() {
        Double countCurrent = userReportRepository.countAllInCurrentMonth();
        Double countPrevious = getCurrentMonth() == 1
                ? userReportRepository.countAllInPreviousMonthAndYear()
                : userReportRepository.countAllInPreviousMonth();
        return countPrevious != 0 ? (countCurrent / countPrevious) - 1 : 0;
    }

    public Long countBlogByCategory(Long categoryId) {
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(categoryId);
        List<BlogPostEntity> list = findBlogByCategory(categoryOptional.get().getName(),
                categoryOptional.get().getParentCategory() == null ? null
                        : categoryOptional.get().getParentCategory().getId());
        return list != null ? list.size() : 0L;
    }

    public List<BlogPostEntity> findBlogByCategory(String name, Long parentCategoryId) {
        Optional<CategoryEntity> categoryEntity = findCategoryByNameAndParentId(name, parentCategoryId);
        if (categoryEntity.isPresent()) {
            List<CategoryEntity> categoryEntityList = findCategoryToSearch(categoryEntity.get(), new ArrayList<>());
            if (!categoryEntityList.isEmpty()) {
                List<BlogPostEntity> blogPostEntity = new ArrayList<>();
                Set<BlogPostEntity> blogPostSet = new HashSet<>();
                List<BlogPostEntity> blogPostEntityList = blogPostRepository.findByCategoryInAndIsApprovedTrueAndStatusTrue(categoryEntityList);
                if (blogPostEntityList != null) {
                    blogPostEntity.addAll(blogPostEntityList);
                }
                for (BlogPostEntity entity : blogPostEntity) {
                    blogPostSet.add(entity);
                }
                return blogPostEntityList;
            } else return new ArrayList<>();
        } else throw new BlogPostException("Category doesn't exists");
    }

    public List<CategoryEntity> findCategoryToSearch(CategoryEntity categoryEntity, List<CategoryEntity> categoryEntityList) {
        List<CategoryEntity> subEntityList = categoryRepository.findByParentCategory(categoryEntity);
        if (subEntityList.isEmpty()) {
            categoryEntityList.add(categoryEntity);
        } else {
            categoryEntityList.add(categoryEntity);
            for (CategoryEntity entity : subEntityList) {
                findCategoryToSearch(entity, categoryEntityList);
            }
        }
        return categoryEntityList;
    }

    public Optional<CategoryEntity> findCategoryByNameAndParentId(String name, Long parentCategoryId) {
        CategoryEntity parentCategory = categoryRepository.findParentCategoryById(parentCategoryId);
        return categoryRepository.findByNameAndParentCategory(name, parentCategory);
    }
}
