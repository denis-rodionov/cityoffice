package com.rodionov.cityoffice.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rodionov.cityoffice.config.RealDatabaseTestConfiguration;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RealDatabaseTestConfiguration.class })
public class ProjectServiceTests {

	@InjectMocks
	private ProjectService projectService;
		
	@Mock
	private UserRepository userRepository;
	
	@Before
	public void cleanup() {		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getUsersToNotifyTest() {
		// arrange
		String projectId = "123";
		when(userRepository.findAll()).thenReturn(Arrays.asList(
			new User("1", "1", "1", Arrays.asList("111", projectId)),
			new User("2", "2", "2", Arrays.asList("333"))
		));
		
		// act
		List<User> actual = projectService.getUsersToNotify(projectId);
		
		// assert
		assertThat(actual).hasSize(1);
		assertThat(actual.get(0).getId()).isEqualTo("1");
	}
	
	@Test
	public void getUsersToNotifyWithBlankUsersTest() {
		// arrange
		String projectId = "123";
		when(userRepository.findAll()).thenReturn(Arrays.asList(
			new User("1", "1", "1", Arrays.asList("111", projectId)),
			new User("2", "2", "2", null)
		));
		
		// act
		List<User> actual = projectService.getUsersToNotify(projectId);
		
		// assert
		assertThat(actual).hasSize(1);
		assertThat(actual.get(0).getId()).isEqualTo("1");
	}
}
