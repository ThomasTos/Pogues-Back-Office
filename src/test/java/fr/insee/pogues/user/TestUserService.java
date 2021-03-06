package fr.insee.pogues.user;

import fr.insee.pogues.user.model.User;
import fr.insee.pogues.user.query.UserServiceQuery;
import fr.insee.pogues.user.service.UserService;
import fr.insee.pogues.user.service.UserServiceImpl;
import fr.insee.pogues.webservice.rest.PoguesException;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestUserService {

    @Mock
    private UserServiceQuery userServiceQuery;

    @InjectMocks
    private UserService userService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp(){
        userService = spy(new UserServiceImpl());
        initMocks(this);
    }

    @Test
    public void getUserId() throws Exception {
        HttpServletRequest hsr = mock(HttpServletRequest.class);
        when(hsr.getUserPrincipal()).thenReturn(/*getName*/() -> "foo");
        JSONObject response = userService.getUserID(hsr);
        assertEquals("foo", response.get("id"));
    }

    @Test
    public void getUserIdWithException() throws Exception {
        exception.expect(PoguesException.class);
        exception.expectMessage("Not authenticated");
        HttpServletRequest hsr = mock(HttpServletRequest.class);
        when(hsr.getUserPrincipal()).thenReturn(null);
        JSONObject response = userService.getUserID(hsr);
    }

    @Test
    public void getNameAndPermission() throws Exception {
        HttpServletRequest hsr = mock(HttpServletRequest.class);
        when(hsr.getUserPrincipal()).thenReturn(/*getName*/() -> "foo");
        when(userServiceQuery.getNameAndPermissionByID("foo"))
                .thenReturn(new User("DGOWGQ","John Doe","John","Doe","DR14-DIR"));
        User user = userService.getNameAndPermission(hsr);
        assertEquals("DGOWGQ", user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("DR14-DIR", user.getPermission());
    }

    @Test
    public void getPermissions() throws Exception {
        when(userServiceQuery.getPermissions())
                .thenReturn(new ArrayList<String>(){
                    { add("DG75-C002"); }
                    { add("G75-L101"); }
                    { add("DR14-DIR"); }
                });
        List<String> permissions = userService.getPermissions();
        assertEquals("DG75-C002", permissions.get(0));
        assertEquals("G75-L101", permissions.get(1));
        assertEquals("DR14-DIR", permissions.get(2));
    }

    @Test
    public void getNameAndPermissionWithException() throws Exception {
        exception.expect(NamingException.class);
        HttpServletRequest hsr = mock(HttpServletRequest.class);
        when(hsr.getUserPrincipal()).thenReturn(/*getName*/() -> "foo");
        when(userServiceQuery.getNameAndPermissionByID("foo"))
                .thenThrow(new NamingException());
        userService.getNameAndPermission(hsr);
    }

    @Test
    public void getPermissionsWithException() throws Exception {
        exception.expect(NamingException.class);
        when(userServiceQuery.getPermissions())
                .thenThrow(new NamingException());
        userService.getPermissions();

    }
}
