XELATEX=xelatex
BIBTEX=bibtex

document.pdf: document.tex *.tex
	$(XELATEX) $<
	-$(BIBTEX) document
	$(XELATEX) $<
	$(XELATEX) $<

.PHONY: clean doc

clean:
	rm -rf *.pdf
	rm -rf *.aux
	rm -rf *.log
	rm -rf *.bbl
	rm -rf *.blg
	rm -rf *.lof
	rm -rf *.lot
	rm -rf *.toc
	rm -rf *.out

doc: document.pdf
