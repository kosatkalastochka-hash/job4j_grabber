package service;

import model.Post;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HabrCareerParse implements Parse {
    private static final Logger LOG = Logger.getLogger(HabrCareerParse.class);
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PREFIX = "/vacancies?page=";
    private static final String SUFFIX = "&q=Java%20developer&type=all";
    private static final short PAGE = 5;

    public List<Post> fetch() {
        var result = new ArrayList<Post>();
        try {
            for (int pageNumber = 1; pageNumber <= PAGE; pageNumber++) {
                String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
                var connection = Jsoup.connect(fullLink);
                var document = connection.get();
                var rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    var titleElement = row.select(".vacancy-card__title").first();
                    var linkElement = titleElement.child(0);
                    var timeElement = row.select(".vacancy-card__date").first().select(".basic-date").first();
                    Long time = OffsetDateTime.parse(timeElement.attr("datetime")).toInstant().toEpochMilli();
                    String vacancyName = titleElement.text();
                    String link = String.format("%s%s", SOURCE_LINK,
                            linkElement.attr("href"));
                    var post = new Post();
                    post.setTitle(vacancyName);
                    post.setLink(link);
                    post.setTime(time);
                    result.add(post);
                });
            }
        } catch (IOException e) {
            LOG.error("When load page", e);
        }
        return result;
    }

    private String retrieveDescription(String link) {
        String fullLink = "%s%s".formatted(SOURCE_LINK, link);
        var connection = Jsoup.connect(fullLink);
        String result = null;
        List<Element> description = new ArrayList<>();
        try {
            var document = connection.get();
            var jobTitle = document.select(".page-title__title").first();
            description.add(jobTitle);
            var salary = document.select(".predicted-salary__title").first();
            description.add(salary);
            description.add(null);
            var requirements = document.select(".content-section__title").first();
            description.add(requirements);
            var requirement1 = document.select(".chip-with-icon__text").first();
            description.add(requirement1);
            var requirement2 = document.select(".chip-with-icon__text").get(1);
            description.add(requirement2);
            var requirement3 = document.select(".chip-without-icon__text");
            description.addAll(requirement3);
            description.add(null);
            var conditions = document.select(".content-section__title").get(1);
            description.add(conditions);
            var condition1 = document.select(".chip-with-icon__text").get(2);
            description.add(condition1);
            var condition2 = document.select(".chip-with-icon__text").last();
            description.add(condition2);
            description.add(null);
            var company = document.select(".content-section__title").last();
            description.add(company);
            var company1 = document.select(".link-comp").first();
            description.add(company1);
            var company2 = document.select(".vacancy-company__sub-title").first();
            description.add(company2);
            var company3 = document.select(".link-comp").get(1);
            description.add(company3);
            description.add(null);
            var jobDescription = document.select(".section-title__title").getFirst();
            description.add(jobDescription);
            var jobDescription1 = document.select(".style-ugc").first();
            description.add(jobDescription1);
            result = description.stream()
                    .map(Optional::ofNullable)
                    .map(element -> element.map(Element::wholeText).orElse(""))
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            LOG.error("When load description", e);
        }
        return result;
    }
}


