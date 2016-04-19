package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import org.apache.commons.lang3.Validate;

import play.mvc.Http.Cookies;
import play.mvc.Http.RawBuffer;
import play.mvc.Http.Request;

import com.google.common.collect.Lists;

public class PlayHttpServletRequest implements HttpServletRequest {

  private static final String AMPERSAND = "&";
  private static final String CONTENT_TYPE = "Content-Type";
  private static final String EQUALS = "=";
  private static final String QUESTION_MARK = "\\?";
  private static final String SEMI_COLON = ";";

  private Request req;

  /**
   * Instantiates a new play http servlet request.
   * @param request the Play framework Request
   */
  public PlayHttpServletRequest(Request request) {
    Validate.notNull(request, "Cannot create PlayHttpServletRequest from null Play Request!");
    Validate.notNull(request.body(), "Cannot create PlayHttpServletRequest from null-body Play Request!");
    Validate.notNull(request._underlyingHeader(), "Cannot create PlayHttpServletRequest from null-header Play Request!");
    Validate.notNull(request._underlyingRequest(), "Cannot create PlayHttpServletRequest from null-request Play Request!");
    Validate.notNull(request.queryString(), "Cannot create PlayHttpServletRequest from null-queryString Play Request!");
    req = request;
  }

  /**
   * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
   */
  @Override
  public Object getAttribute(String name) {
    return null;
  }

  /**
   * @see javax.servlet.ServletRequest#getAttributeNames()
   */
  @Override
  public Enumeration getAttributeNames() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#getCharacterEncoding()
   */
  @Override
  public String getCharacterEncoding() {
    Map<String, String[]> headers = req.headers();
    if (headers != null && headers.containsKey(CONTENT_TYPE)) {
      String[] contentType = headers.get(CONTENT_TYPE);
      if (contentType != null && contentType.length > 0) {
        return contentType[0].split(SEMI_COLON)[1].trim();
      }
    }
    return null;
  }

  /**
   * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
   */
  @Override
  public void setCharacterEncoding(String env) {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#getContentLength()
   */
  @Override
  public int getContentLength() {
    RawBuffer rawBody = req.body().asRaw();
    if (rawBody != null) {
      Long size = rawBody.size();
      if (size != null && size >= 0) {
        int intSize = size.intValue();
        if (intSize == size) {
          return intSize;
        }
      }
    }
    return -1;
  }

  /**
   * @see javax.servlet.ServletRequest#getContentLengthLong()
   */
  @Override
  public long getContentLengthLong() {
    RawBuffer rawBody = req.body().asRaw();
    if (rawBody != null) {
      Long size = rawBody.size();
      if (size != null && size >= 0) {
        return size;
      }
    }
    return -1;
  }

  /**
   * @see javax.servlet.ServletRequest#getContentType()
   */
  @Override
  public String getContentType() {
    Map<String, String[]> headers = req.headers();
    if (headers != null && headers.containsKey(CONTENT_TYPE)) {
      String[] contentType = headers.get(CONTENT_TYPE);
      if (contentType != null && contentType.length > 0) {
        return contentType[0].split(SEMI_COLON)[0].trim();
      }
    }
    return null;
  }

  /**
   * @see javax.servlet.ServletRequest#getInputStream()
   */
  @Override
  public ServletInputStream getInputStream() throws IOException {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
   */
  @Override
  public String getParameter(String name) {
    String[] values = getParameterValues(name);
    return values != null && values.length > 0 ? values[0] : null;
  }

  /**
   * @see javax.servlet.ServletRequest#getParameterNames()
   */
  @Override
  public Enumeration getParameterNames() {
    Map<String, String[]> keyValues = req.queryString();
    return Collections.enumeration(keyValues == null ? new ArrayList<>() : keyValues.keySet());
  }

  /**
   * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
   */
  @Override
  public String[] getParameterValues(String name) {
    Map<String, String[]> keyValues = req.queryString();
    return keyValues != null && keyValues.containsKey(name) ? keyValues.get(name) : null;
  }

  /**
   * @see javax.servlet.ServletRequest#getParameterMap()
   */
  @Override
  public Map getParameterMap() {
    return req.queryString();
  }

  /**
   * @see javax.servlet.ServletRequest#getProtocol()
   */
  @Override
  public String getProtocol() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#getScheme()
   */
  @Override
  public String getScheme() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#getServerName()
   */
  @Override
  public String getServerName() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#getServerPort()
   * Return -1 if could not determine port.
   */
  @Override
  public int getServerPort() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#getReader()
   */
  @Override
  public BufferedReader getReader() throws IOException {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#getRemoteAddr()
   */
  @Override
  public String getRemoteAddr() {
    return req._underlyingHeader().remoteAddress();
  }

  /**
   * @see javax.servlet.ServletRequest#getRemoteHost()
   */
  @Override
  public String getRemoteHost() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#setAttribute(java.lang.String,
   *      java.lang.Object)
   */
  @Override
  public void setAttribute(String name, Object o) {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
   */
  @Override
  public void removeAttribute(String name) {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#getLocale()
   */
  @Override
  public Locale getLocale() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#getLocales()
   */
  @Override
  public Enumeration getLocales() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#isSecure()
   */
  @Override
  public boolean isSecure() {
    return req.secure();
  }

  /**
   * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
   */
  @Override
  public RequestDispatcher getRequestDispatcher(String path) {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
   */
  @Override
  public String getRealPath(String path) {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#getRemotePort()
   */
  @Override
  public int getRemotePort() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#getLocalName()
   */
  @Override
  public String getLocalName() {
    return req._underlyingHeader().domain();
  }

  /**
   * @see javax.servlet.ServletRequest#getLocalAddr()
   */
  @Override
  public String getLocalAddr() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.ServletRequest#getLocalPort()
   */
  @Override
  public int getLocalPort() {
    throw new RuntimeException("Method not implemented!");
  }

  @Override
  public ServletContext getServletContext() {
    throw new RuntimeException("Method not implemented");
  }

  /**
   * @see javax.servlet.ServletRequest#startAsync()
   */
  @Override
  public AsyncContext startAsync() throws IllegalStateException {
    throw new IllegalStateException("Cannot start async for PlayHttpServletRequest!");
  }

  /**
   * @see javax.servlet.ServletRequest#startAsync(ServletRequest, ServletResponse)
   */
  @Override
  public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
    throw new IllegalStateException("Cannot start async for PlayHttpServletRequest!");
  }

  /**
   * @see javax.servlet.ServletRequest#isAsyncStarted()
   */
  @Override
  public boolean isAsyncStarted() {
    return false;
  }

  /**
   * @see javax.servlet.ServletRequest#isAsyncSupported()
   */
  @Override
  public boolean isAsyncSupported() {
    return false;
  }

  /**
   * @see javax.servlet.ServletRequest#getAsyncContext()
   */
  @Override
  public AsyncContext getAsyncContext() {
    throw new IllegalStateException("Cannot get async context for PlayHttpServletRequest!");
  }

  /**
   * @see javax.servlet.ServletRequest#getDispatcherType()
   */
  @Override
  public DispatcherType getDispatcherType() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getAuthType()
   */
  @Override
  public String getAuthType() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getCookies()
   */
  @Override
  public Cookie[] getCookies() {
    List<Cookie> toReturn = new ArrayList<>();
    Cookies cookies = req.cookies();
    if (cookies != null) {
      Iterator<play.mvc.Http.Cookie> iterator = cookies.iterator();
      while (iterator.hasNext()) {
        toReturn.add(createCookie(iterator.next()));
      }
    }
    return toReturn.isEmpty() ? null : toReturn.toArray(new Cookie[]{});
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
   */
  @Override
  public long getDateHeader(String name) {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
   */
  @Override
  public String getHeader(String name) {
    Map<String, String[]> headers = req.headers();
    for (Entry<String, String[]> header : headers.entrySet()) {
      if (header.getKey().equalsIgnoreCase(name)) {
        String[] values = header.getValue();
        if (values != null && values.length > 0) {
          return values[0];
        }
      }
    }
    return null;
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
   */
  @Override
  public Enumeration getHeaders(String name) {
    Map<String, String[]> headers = req.headers();
    if (headers == null) {
      return null;
    }
    List<String> list = new ArrayList<>();
    if (headers.containsKey(name)) {
      String[] values = headers.get(name);
      if (values != null) {
        list = Lists.newArrayList(values);
      }
    }
    return Collections.enumeration(list);
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
   */
  @Override
  public Enumeration getHeaderNames() {
    Map<String, String[]> headers = req.headers();
    if (headers == null) {
      return null;
    }
    return Collections.enumeration(headers.keySet());
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
   */
  @Override
  public int getIntHeader(String name) {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getMethod()
   */
  @Override
  public String getMethod() {
    return req.method();
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getPathInfo()
   */
  @Override
  public String getPathInfo() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
   */
  @Override
  public String getPathTranslated() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getContextPath()
   */
  @Override
  public String getContextPath() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getQueryString()
   */
  @Override
  public String getQueryString() {
    StringBuilder stringBuilder = new StringBuilder();
    Map<String, String[]> queryMap = req.queryString();
    boolean first = true;
    for (Entry<String, String[]> queryEntry : queryMap.entrySet()) {
      String[] values = queryEntry.getValue();
      if (values != null) {
        for (String value : values) {
          if (!first) {
            stringBuilder.append(AMPERSAND);
          }
          stringBuilder.append(queryEntry.getKey() + EQUALS + value);
          first = false;
        }
      }
    }
    return stringBuilder.length() > 0 ? stringBuilder.toString() : null;
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
   */
  @Override
  public String getRemoteUser() {
    return req.username();
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
   */
  @Override
  public boolean isUserInRole(String role) {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
   */
  @Override
  public Principal getUserPrincipal() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
   */
  @Override
  public String getRequestedSessionId() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getRequestURI()
   */
  @Override
  public String getRequestURI() {
    return req.uri().split(QUESTION_MARK)[0];
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getRequestURL()
   */
  @Override
  public StringBuffer getRequestURL() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getServletPath()
   */
  @Override
  public String getServletPath() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
   */
  @Override
  public HttpSession getSession(boolean create) {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getSession()
   */
  @Override
  public HttpSession getSession() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#changeSessionId()
   */
  @Override
  public String changeSessionId() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
   */
  @Override
  public boolean isRequestedSessionIdValid() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
   */
  @Override
  public boolean isRequestedSessionIdFromCookie() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
   */
  @Override
  public boolean isRequestedSessionIdFromURL() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
   */
  @Override
  public boolean isRequestedSessionIdFromUrl() {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#authenticate(HttpServletResponse)
   */
  @Override
  public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#login(String, String)
   */
  @Override
  public void login(String username, String password) throws ServletException {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#logout()
   */
  @Override
  public void logout() throws ServletException {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getParts()
   */
  @Override
  public Collection<Part> getParts() throws IOException, ServletException {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#getPart(String)
   */
  @Override
  public Part getPart(String name) throws IOException, ServletException {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @see javax.servlet.http.HttpServletRequest#upgrade(Class)
   */
  @Override
  public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
    throw new RuntimeException("Method not implemented!");
  }

  /**
   * @param cookie - the play.mvc.Http.Cookie to convert to javax.servlet.http.Cookie
   * @return the javax.servlet.http.Cookie created from the play.mvc.Http.Cookie
   */
  private static javax.servlet.http.Cookie createCookie(play.mvc.Http.Cookie cookie) {
    Cookie newCookie = new javax.servlet.http.Cookie(cookie.name(), cookie.value());
    newCookie.setHttpOnly(cookie.httpOnly());
    newCookie.setSecure(cookie.secure());
    Integer maxAge = cookie.maxAge();
    newCookie.setMaxAge(maxAge == null ? -1 : (maxAge < 0 ? 0 : maxAge));
    String path = cookie.path();
    if (path != null) {
      newCookie.setPath(path);
    }
    String domain = cookie.domain();
    if (domain != null) {
      newCookie.setDomain(domain);
    }
    return newCookie;
  }
}