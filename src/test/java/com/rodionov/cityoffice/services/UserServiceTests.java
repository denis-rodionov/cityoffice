package com.rodionov.cityoffice.services;

import static org.fest.assertions.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rodionov.cityoffice.config.RealDatabaseTestConfiguration;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.model.UserProject;
import com.rodionov.cityoffice.model.UserVacation;
import com.rodionov.cityoffice.repository.UserProjectRepository;
import com.rodionov.cityoffice.repository.UserRepository;
import com.rodionov.cityoffice.repository.UserVacationRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RealDatabaseTestConfiguration.class })
public class UserServiceTests {

	@Autowired
	@InjectMocks
	private UserService userService;
	
	@Autowired
	private UserProjectRepository userProjectRepository;
	
	@Autowired
	private UserVacationRepository userVacationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Before
	public void cleanup() {
		userProjectRepository.deleteAll();
		userVacationRepository.deleteAll();
		userRepository.deleteAll();
	}
	
	//------------------  getUserProjects ---------------------------

	@Test
	public void testUserProjectWithoutParameters() {
		// arrange
		String project1 = "Project #1";
		String project2 = "Project #2";
		String user1 = "User #1";
		LocalDate startDate = LocalDate.of(2016, 4, 1);
		LocalDate finishDate = LocalDate.of(2016, 4, 30);
		userProjectRepository.save(new UserProject(project1, user1, startDate, finishDate, 0.5));
		userProjectRepository.save(new UserProject(project2, user1, startDate, finishDate, 0.5));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<UserProject> actual = userService.getUserProjects(null, null, null, null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(2);
	}
	
	@Test
	public void testUserProjectWithoutParameters2() {
		// arrange
		String project1 = "Project #1";
		String project2 = "Project #2";
		String user1 = "User #1";
		LocalDate startDate = LocalDate.of(2016, 4, 1);
		LocalDate finishDate = LocalDate.of(2016, 4, 30);
		userProjectRepository.save(new UserProject(project1, user1, startDate, finishDate, 0.5));
		userProjectRepository.save(new UserProject(project2, user1, startDate, finishDate, 0.5));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<UserProject> actual = userService.getUserProjects(new ArrayList<String>(), null, null, null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(2);
	}
	
	@Test
	public void testUserProjectByProject() {
		// arrange
		String project1 = "Project #1";
		String project2 = "Project #2";
		String user1 = "User #1";
		LocalDate startDate = LocalDate.of(2016, 4, 1);
		LocalDate finishDate = LocalDate.of(2016, 4, 30);
		userProjectRepository.save(new UserProject(project1, user1, startDate, finishDate, 0.5));
		UserProject userProject2 = userProjectRepository.save(new UserProject(project2, user1, startDate, finishDate, 0.5));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<UserProject> actual = userService.getUserProjects(Arrays.asList(project2), null, null, null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(1);
		assertThat(actual.getContent().get(0).getId()).isEqualTo(userProject2.getId());
	}
	
	@Test
	public void testUserProjectByUser() {
		// arrange
		String project1 = "Project #1";
		String project2 = "Project #2";
		String user1 = "User#1";
		String user2 = "User#2";
		LocalDate startDate = LocalDate.of(2016, 4, 1);
		LocalDate finishDate = LocalDate.of(2016, 4, 30);
		
		userProjectRepository.save(new UserProject(project1, user1, startDate, finishDate, 1));
		UserProject userProject2 = userProjectRepository.save(new UserProject(project2, user2, startDate, finishDate, 1));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<UserProject> actual = userService.getUserProjects(Arrays.asList(project1, project2), user2, null, null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(1);
		assertThat(actual.getContent().get(0).getId()).isEqualTo(userProject2.getId());
	}
	
	@Test
	public void testUserProjectByStartBeforeTime() {
		// arrange
		String project1 = "Project #1";
		String project2 = "Project #2";
		User user1 = userRepository.save(new User("Svetlana", "s@gmail.com"));
		User user2 = userRepository.save(new User("Tatiana", "t@gmail.com"));
		LocalDate finishDate = LocalDate.of(2016, 4, 30);
		
		userProjectRepository.save(new UserProject(project1, user1.getId(), LocalDate.of(2016, 1, 15), finishDate, 1));
		userProjectRepository.save(new UserProject(project2, user2.getId(), LocalDate.of(2016, 1, 30), finishDate, 1));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<UserProject> actual = userService.getUserProjects(null, null, LocalDate.of(2016, 2, 10), null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(2);
	}
	
	@Test
	public void testUserProjectByStartBeforeTime2() {
		// arrange
		String project1 = "Project #1";
		String project2 = "Project #2";
		User user1 = userRepository.save(new User("Svetlana", "s@gmail.com"));
		User user2 = userRepository.save(new User("Tatiana", "t@gmail.com"));
		LocalDate finishDate = LocalDate.of(2016, 4, 30);
		
		UserProject up1 = userProjectRepository.save(new UserProject(project1, user1.getId(), LocalDate.of(2016, 1, 15), finishDate, 1));
		userProjectRepository.save(new UserProject(project2, user2.getId(), LocalDate.of(2016, 1, 30), finishDate, 1));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<UserProject> actual = userService.getUserProjects(null, null, LocalDate.of(2016, 1, 20), null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(1);
		assertThat(actual.getContent().get(0).getId()).isEqualTo(up1.getId());
	}
	
	@Test
	public void testUserProjectByFinishAfterTime() {
		// arrange
		String project1 = "Project #1";
		String project2 = "Project #2";
		User user1 = userRepository.save(new User("Svetlana", "s@gmail.com"));
		User user2 = userRepository.save(new User("Tatiana", "t@gmail.com"));
		
		userProjectRepository.save(new UserProject(project1, user1.getId(), 
				LocalDate.of(2016, 1, 15), LocalDate.of(2016, 2, 15), 1));
		userProjectRepository.save(new UserProject(project2, user2.getId(), 
				LocalDate.of(2016, 1, 30), LocalDate.of(2016, 2, 28), 1));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<UserProject> actual = userService.getUserProjects(null, null, null, LocalDate.of(2016, 2, 10), pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(2);
	}
	
	@Test
	public void testUserProjectByFinishAfterTime2() {
		// arrange
		String project1 = "Project #1";
		String project2 = "Project #2";
		User user1 = userRepository.save(new User("Svetlana", "s@gmail.com"));
		User user2 = userRepository.save(new User("Tatiana", "t@gmail.com"));
		
		userProjectRepository.save(new UserProject(project1, user1.getId(), 
				LocalDate.of(2016, 1, 15), LocalDate.of(2016, 2, 15), 1));
		UserProject up2 = userProjectRepository.save(new UserProject(project2, user2.getId(), 
				LocalDate.of(2016, 1, 30), LocalDate.of(2016, 2, 28), 1));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<UserProject> actual = userService.getUserProjects(null, null, null, LocalDate.of(2016, 2, 20), pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(1);
		assertThat(actual.getContent().get(0).getId()).isEqualTo(up2.getId());
	}
	
	//-----------------  getUserVacations ---------------------------
	@Test
	public void testUserVacationsWithoutParameters() {
		// arrange
		String user1 = "User #1";
		LocalDate startDate = LocalDate.of(2016, 4, 1);
		LocalDate finishDate = LocalDate.of(2016, 4, 30);
		userVacationRepository.save(new UserVacation(user1, startDate, finishDate));
		userVacationRepository.save(new UserVacation(user1, startDate, finishDate));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<UserVacation> actual = userService.getUserVacations(null, null, null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(2);
	}
	
	@Test
	public void testUserVacationByUser() {
		// arrange
		String user1 = "User #1";
		String user2 = "User #2";
		LocalDate startDate = LocalDate.of(2016, 4, 1);
		LocalDate finishDate = LocalDate.of(2016, 4, 30);
		userVacationRepository.save(new UserVacation(user1, startDate, finishDate));
		UserVacation uv2 = userVacationRepository.save(new UserVacation(user2, startDate, finishDate));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<UserVacation> actual = userService.getUserVacations(user2, null, null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(1);
		assertThat(actual.getContent().get(0).getId()).isEqualTo(uv2.getId());
	}
	
	@Test
	public void testUserVacationByStartBeforeTime() {
		// arrange
		String user1 = "User #1";
		LocalDate finishDate = LocalDate.of(2016, 4, 30);
		
		userVacationRepository.save(new UserVacation(user1, LocalDate.of(2016, 1, 15), finishDate));
		userVacationRepository.save(new UserVacation(user1, LocalDate.of(2016, 1, 30), finishDate));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<UserVacation> actual = userService.getUserVacations(null, LocalDate.of(2016, 2, 10), null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(2);
	}
	
	@Test
	public void testUserVacationByStartBeforeTime2() {
		// arrange
		String user1 = "User #1";
		LocalDate finishDate = LocalDate.of(2016, 4, 30);
		
		UserVacation uv1 = userVacationRepository.save(new UserVacation(user1, LocalDate.of(2016, 1, 15), finishDate));
		userVacationRepository.save(new UserVacation(user1, LocalDate.of(2016, 1, 30), finishDate));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<UserVacation> actual = userService.getUserVacations(null, LocalDate.of(2016, 1, 20), null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(1);
		assertThat(actual.getContent().get(0).getId()).isEqualTo(uv1.getId());
	}
	
	@Test
	public void testUserVacationByFinishAfterTime() {
		// arrange
		String user1 = "User #1";
		
		userVacationRepository.save(new UserVacation(user1, 
				LocalDate.of(2016, 1, 15), LocalDate.of(2016, 2, 15)));
		userVacationRepository.save(new UserVacation(user1, 
				LocalDate.of(2016, 1, 30), LocalDate.of(2016, 2, 28)));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<UserVacation> actual = userService.getUserVacations(null, null, LocalDate.of(2016, 2, 10), pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(2);
	}
	
	@Test
	public void testUserVacationByFinishAfterTime2() {
		
		// arrange
		String user1 = "User #1";
		
		userVacationRepository.save(new UserVacation(user1, 
				LocalDate.of(2016, 1, 15), LocalDate.of(2016, 2, 15)));
		UserVacation uv2 = userVacationRepository.save(new UserVacation(user1, 
				LocalDate.of(2016, 1, 30), LocalDate.of(2016, 2, 28)));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<UserVacation> actual = userService.getUserVacations(null, null, LocalDate.of(2016, 2, 20), pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(1);
		assertThat(actual.getContent().get(0).getId()).isEqualTo(uv2.getId());
	}
	
}
