package com.example.leadManagementSystem2.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.leadManagementSystem2.Entity.BusinessAssociate;
import com.example.leadManagementSystem2.Entity.BusinessAssociateHistory;
import com.example.leadManagementSystem2.Entity.EmployeeDetails;
import com.example.leadManagementSystem2.Entity.Leads;
import com.example.leadManagementSystem2.Entity.Users_Credentials;
import com.example.leadManagementSystem2.Repository.BusinessAssociateRepository;
import com.example.leadManagementSystem2.Repository.EmployeeDetailsRepository;
import com.example.leadManagementSystem2.Repository.LeadsRepository;
import com.example.leadManagementSystem2.Repository.User_Credentials_Repository;
import com.example.leadManagementSystem2.Service.BusinessAssociateService;
import com.example.leadManagementSystem2.Service.DataFetchingService;
import com.example.leadManagementSystem2.Service.EmployeeService;
import com.example.leadManagementSystem2.Service.UserService;

@Controller
@RequestMapping("/Admin")
public class AdminController {

	@Autowired
	User_Credentials_Repository user_Credentials_Repository;
	@Autowired
	LeadsRepository leadsRepository;
	
	@Autowired
	private UserService userService;

	@Autowired
	DataFetchingService dataFetchingService;
	
	@Autowired
	BusinessAssociateRepository businessAssociateRepository;
	
	@Autowired
	BusinessAssociateService businessAssociateService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;
	
	@GetMapping("/admin_Dashboard")
	public String getAdminDashboard(ModelMap model ) {
		model.put("userName", "Tanmay");
		return "Admin/Admin_Dashboard";
	}
	
	@GetMapping("/registration")
	public String getAccountRegistrationPage() {
		return "Admin/AddEmployeeForm";
	}
	
	
	@GetMapping("/admin_Dashboard/Leads")
	public String getAllLeads(ModelMap model) {
		
		model.addAttribute("Leads", dataFetchingService.getAllLeadsDetails());
		return "Admin/Leads";
	}
	
	
	
	@PostMapping("/saveUser")
	public String CreateAccount(@ModelAttribute EmployeeDetails employeeDetails) {
		
		/* Users_Credentials users_Credentials1 =  userService.saveUser(Users_credentials);*/
		
		employeeService.saveEmployeeDetails(employeeDetails);

		
		return "redirect:/Admin/registration";
	}
	
	@GetMapping("/admin_Dashboard/Leads/edit/{id}")
	public String getEditLeadsPage(@PathVariable Long id, Model model) {
		
		model.addAttribute("Leads", leadsRepository.findById(id).get());
		return "Admin/edit_Leads";
	}
	
	@PostMapping("/admin_Dashboard/Leads/edit/{id}")
	public String updateLead(@PathVariable Long id, @ModelAttribute Leads leads ) {
		
		Leads existingLead = leadsRepository.findById(id).get();
		
		existingLead.setName(leads.getName());
		existingLead.setEmail(leads.getEmail());
		existingLead.setPhone(leads.getPhone());
		existingLead.setAddress(leads.getAddress());
		existingLead.setCourse(leads.getCourse());
		existingLead.setMessage(leads.getMessage());
		existingLead.setLeadStatus(leads.getLeadStatus());
		
		leadsRepository.save(existingLead);
		return "redirect:/Admin/admin_Dashboard/Leads";
	}
	
	@GetMapping("/admin_Dashboard/Leads/{id}")
	public String deleteLead(@PathVariable Long id) {
		leadsRepository.deleteById(id);
		return "redirect:/Admin/admin_Dashboard/Leads";
	}
	
	
	
	@GetMapping("/admin_Dashboard/ApproveBusinessAssociate")
	public String getApproveBusinessAssociatePage(ModelMap model) {
		
		List<BusinessAssociate> approvedBAs = businessAssociateRepository.findByApproval(false);
		model.addAttribute("approvedBusinessAssociates", approvedBAs);
		
		return "Admin/ApproveBusinessAssociate";
	}
	
	@GetMapping("/admin_Dashboard/approve/{id}")
	public String approveBA(@PathVariable Long id) {
		
		
		BusinessAssociate ba = businessAssociateService.approveBusinessAssociate(id);
		
		return "redirect:/Admin/admin_Dashboard/ApproveBusinessAssociate";
	}
	
	
	@GetMapping("/admin_Dashboard/reject/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String rejectBusinessAssociate(@PathVariable Long id, @ModelAttribute BusinessAssociateHistory businessAssociateHistory ) {

		
		businessAssociateService.rejectBusinessAssociate(id, businessAssociateHistory);
		
		
		
		return "redirect:/Admin/admin_Dashboard/ApproveBusinessAssociate";
	}
	
	
	
	@PostMapping("/admin_Dashboard/reject/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String rejectBA(@PathVariable Long id, @ModelAttribute BusinessAssociateHistory businessAssociateHistory ) {

		
		businessAssociateService.rejectBusinessAssociate(id, businessAssociateHistory);
		
		
		
		return "redirect:/Admin/admin_Dashboard/ApproveBusinessAssociate";
	}
	
	
	@GetMapping("/profile")
	public String getProfile(Model model, Principal principal) {
		
		String Usename = principal.getName();
		System.out.println(Usename);
		
		Users_Credentials user =   user_Credentials_Repository.getUsersCredentialsByUserName(Usename);
		Long id = user.getId();
		
		EmployeeDetails employeeDetails =   employeeDetailsRepository.getEmployeeDetailsByid(id);
		
		
		System.out.println(employeeDetails);
		model.addAttribute("employeeDetails", employeeDetails);
		
		return "Admin/AdminProfile";
		
	}
	
	
	
	
	
	@GetMapping("/admin_Dashboard/users")
	public String getCallerDetails() {
		return "Admin/UsersDetails";
	}
	
	
}
