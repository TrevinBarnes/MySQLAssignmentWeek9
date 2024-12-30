package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import projects.entity.Project;
import projects.exception.DbException;
import provided.util.DaoBase;

@SuppressWarnings("unused")
public class ProjectDao extends DaoBase {
	private static final String CATEGORY_TABLE = "category";
	private static final String MATERIAL_TABLE = "material";
	private static final String PROJECTS_TABLE = "projects";
	private static final String PROJECTS_CATEGORY_TABLE = "projects_category";
	private static final String STEP_TABLE = "step";
	
	
	public Project insertProjects(Project projects) {
		//formatter:off
		String sql = ""
				+"INSERT INTO "+ PROJECTS_TABLE + " "
				+"(project_name, estimated_hours, acual_hours, difficulty, notes) "
				+"VALUES "
				+"(?,?,?,?,?)";
		//formatter:on
		
		try(Connection conn = DbConnection.getConnection()){
			startTransaction(conn);
			
			try(PreparedStatement stmt = conn.prepareStatement(sql)){
				setParameter(stmt,1,projects.getProjectName(), String.class);
				setParameter(stmt,2,projects.getEstimatedHours(), BigDecimal.class);
				setParameter(stmt,3,projects.getActualHours(), BigDecimal.class);
				setParameter(stmt,4,projects.getDifficulty(), Integer.class);
				setParameter(stmt,5,projects.getNotes(), String.class);
					
				stmt.executeUpdate();
				
				Integer projectId = getLastInsertId(conn, PROJECTS_TABLE);
				commitTransaction(conn);
				
				projects.setProjectId(projectId);
				return projects;
			}
			catch(Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
		}
		catch(SQLException e) {
			throw new DbException(e);
		}
	}
}
