package mx.gob.imss.cit.pmc.cierreanual.commons.dto;

import lombok.Getter;
import lombok.Setter;

public class SftpParamsDTO {

	@Getter
	@Setter
	private String sftpHost;

	@Getter
	@Setter
	private int sftpPort;

	@Getter
	@Setter
	private String sftpUser;

	@Getter
	@Setter
	private String sftpPasword;

}
