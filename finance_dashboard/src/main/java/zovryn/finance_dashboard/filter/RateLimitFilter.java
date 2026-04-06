    package zovryn.finance_dashboard.filter;
    import io.github.bucket4j.Bucket;

    import java.time.Duration;
    import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket getBucket(String ip) {
        return buckets.computeIfAbsent(ip, k -> Bucket.builder()
                .addLimit(limit -> limit.capacity(100).refillGreedy(100, Duration.ofMinutes(15)))
                .build());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();

        if (uri.equals("/api/users/login") || uri.equals("/api/users/register")) {
            String ip = request.getRemoteAddr();
            Bucket bucket = getBucket(ip);

            if (bucket.tryConsume(1)) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(429);
                response.getWriter().write("Too many requests. Please try again later.");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
