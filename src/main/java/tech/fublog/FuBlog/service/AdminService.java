package tech.fublog.FuBlog.service;

import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.dto.response.CategoryResponseDTO;
import tech.fublog.FuBlog.dto.response.CategoryWithNumBlogDTO;
import tech.fublog.FuBlog.dto.response.WeightRatioResponseDTO;
import tech.fublog.FuBlog.dto.response.MonthlyPostCountDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.CategoryEntity;
import tech.fublog.FuBlog.exception.BlogPostException;
import tech.fublog.FuBlog.repository.*;

import java.util.*;
import java.util.stream.Collectors;

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


    public int getCurrentYear() {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int year = calendar.get(Calendar.YEAR) + 1;
        return year;
    }


    public long getCountAllUsers(){
        return userRepository.count();
    }
    public Long countBlogInMonth() {
        Long countBlog = Math.round(blogPostRepository.countAllInCurrentMonth());
        return countBlog;
    }

    public WeightRatioResponseDTO calculateBlogReportWeightRatio() {
        Double countCurrent = blogPostReportRepository.countAllInCurrentMonth();
        Double countPrevious = getCurrentMonth() == 1
                ? blogPostReportRepository.countAllInPreviousMonthAndYear()
                : blogPostReportRepository.countAllInPreviousMonth();
        Double weightRatio =  countPrevious != 0 ? (countCurrent / countPrevious) - 1 : 0;
        return new WeightRatioResponseDTO(countCurrent, countPrevious, weightRatio);
    }

    public WeightRatioResponseDTO calculateUserReportWeightRatio() {
        Double countCurrent = userReportRepository.countAllInCurrentMonth();
        Double countPrevious = getCurrentMonth() == 1
                ? userReportRepository.countAllInPreviousMonthAndYear()
                : userReportRepository.countAllInPreviousMonth();
        Double weightRatio = countPrevious != 0 ? (countCurrent / countPrevious) - 1 : 0;
        return new WeightRatioResponseDTO(countCurrent, countPrevious, weightRatio);
    }

    public WeightRatioResponseDTO calculateBlogWeightRatio() {
        Double countCurrent = blogPostRepository.countAllInCurrentMonth();
        Double countPrevious = getCurrentMonth() == 1
                ? blogPostRepository.countAllInPreviousMonthAndYear()
                : blogPostRepository.countAllInPreviousMonth();
        Double weightRatio = countPrevious != 0 ? (countCurrent / countPrevious) - 1 : 0;
        return new WeightRatioResponseDTO(countCurrent, countPrevious, weightRatio);
    }

    public Long countBlogByCategory(Long categoryId) {
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(categoryId);
        List<BlogPostEntity> list = findBlogByCategory(categoryOptional.get().getName(),
                categoryOptional.get().getParentCategory() == null ? null
                        : categoryOptional.get().getParentCategory().getId());
        return list != null ? list.size() : 0L;
    }

    public List<CategoryWithNumBlogDTO> countBlogByAllCategory() {
        List<CategoryEntity> categoryOptional = categoryRepository.findAll();
        List<CategoryWithNumBlogDTO> dtoList = new ArrayList<>();
        for (CategoryEntity cate : categoryOptional) {
            int numBlog = blogPostRepository.countByCategoryAndIsApprovedTrueAndStatusTrue(cate);
            CategoryWithNumBlogDTO dto = new CategoryWithNumBlogDTO(numBlog, cate.getName());
            dtoList.add(dto);
        }
        return dtoList;
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


public List<MonthlyPostCountDTO> countPostsByMonth() {
    List<Object[]> result = blogPostRepository.countPostsByMonth();

    // Create a map to store post counts by month
    Map<Integer, Long> postCountsByMonth = new HashMap<>();
    result.forEach(array -> postCountsByMonth.put((int) array[0], (long) array[1]));

    // Create a list of MonthlyPostCountDTO with counts for all months
    List<MonthlyPostCountDTO> monthlyPostCounts = new ArrayList<>();
    for (int i = 1; i <= 12; i++) {
        long postCount = postCountsByMonth.getOrDefault(i, 0L);
        monthlyPostCounts.add(new MonthlyPostCountDTO(i, postCount));
    }

    return monthlyPostCounts;
}
}
