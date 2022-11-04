package com.encuentro_musical.anuncios.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginadoDTO {

	private List<PublicationBDTO> publicationBDTO;
	private List<PublicationMDTO> publicationMDTO;
	private int pageNumber;
	private int pageSize;
	private int NumberOfElements;
	private boolean isFirst;
	private boolean isLast;
	private int pagesTotal;
	private Long elementsTotal;

	public PaginadoDTO(int pageNumber, int pageSize, int numberOfElements, boolean isFirst, boolean isLast,
			int totalPage, Long totalElements, List<PublicationBDTO> publicationBDTO, List<PublicationMDTO> publicationMDTO) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.NumberOfElements = numberOfElements;
		this.isFirst = isFirst;
		this.isLast = isLast;
		this.pagesTotal = totalPage;
		this.elementsTotal = totalElements;
		this.publicationBDTO = publicationBDTO;
		this.publicationMDTO = publicationMDTO;
	}

	public PaginadoDTO() {
	}

}
