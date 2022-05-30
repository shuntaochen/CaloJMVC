package core;

import utils.HttpContext;

public interface IFilter {
    void execute(HttpContext httpContext);
}
