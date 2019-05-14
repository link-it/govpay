# Minimal makefile for Sphinx documentation
#

# You can set these variables from the command line.
SPHINXOPTS    =
SPHINXBUILD   = sphinx-build
SPHINXPROJ    = GovPay
SOURCEDIR     = .
BUILDDIR      = _build

html: 
	@echo "Producing html files ..."
	@$(SPHINXBUILD) -M $@ "$(SOURCEDIR)" "$(BUILDDIR)" $(SPHINXOPTS) $(O)
	@echo "Producing html files ok"

clean: 
	rm -rf _build
	rm -rf */_build

pdf: pdf-manuale-installazione pdf-manuale-utente pdf-manuale-integrazione

pdf-manuale-installazione:
	@echo "Producing pdf Manuale di Installazione ..."
	cd installazione; rm -rf pdf; ${SPHINXBUILD} -v -b latex -c .. -Dproject="Guida di Installazione" . "$(BUILDDIR)/pdf"; cd "$(BUILDDIR)/pdf"; make clean all-pdf
	@echo "Producing pdf Manuale Installazione ok"

pdf-manuale-utente:
	@echo "Producing pdf Manuale Utente ..."
	cd utente; rm -rf pdf; ${SPHINXBUILD} -v -b latex -c .. -Dproject="Guida Utente" . "$(BUILDDIR)/pdf"; cd "$(BUILDDIR)/pdf"; make clean all-pdf
	@echo "Producing pdf Manuale Gestione ok"

pdf-manuale-integrazione:
	@echo "Producing pdf Manuale di Integrazione ..."
	cd integrazione; rm -rf pdf; ${SPHINXBUILD} -v -b latex -c .. -Dproject="Guida di Integrazione" . "$(BUILDDIR)/pdf"; cd "$(BUILDDIR)/pdf"; make clean all-pdf
	@echo "Producing pdf Manuale Gestione ok"


# Put it first so that "make" without argument is like "make help".
#help:
#	@$(SPHINXBUILD) -M help "$(SOURCEDIR)" "$(BUILDDIR)" $(SPHINXOPTS) $(O)

#.PHONY: help Makefile

# Catch-all target: route all unknown targets to Sphinx using the new
# "make mode" option.  $(O) is meant as a shortcut for $(SPHINXOPTS).
#%: Makefile
#	@$(SPHINXBUILD) -M $@ "$(SOURCEDIR)" "$(BUILDDIR)" $(SPHINXOPTS) $(O)
