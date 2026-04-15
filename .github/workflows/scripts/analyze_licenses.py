#!/usr/bin/env python3
import os
import sys
import json
import csv
import xml.etree.ElementTree as ET
import re
import urllib.request
import urllib.error
from pathlib import Path
from collections import defaultdict

# Database esteso compatibilità licenze
LICENSE_COMPATIBILITY = {
    # Apache Licenses - tutte le varianti
    'Apache License, Version 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'Apache-2.0'},
    'Apache License, version 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'Apache-2.0'},
    'The Apache Software License, Version 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'Apache-2.0'},
    'Apache Software License, Version 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'Apache-2.0'},
    'Apache Software License - Version 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'Apache-2.0'},
    'Apache 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'Apache-2.0'},
    'Apache 2': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'Apache-2.0'},
    'Apache License 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'Apache-2.0'},
    'Apache-2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'Apache-2.0'},
    'Apache License Version 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'Apache-2.0'},
    'The Apache License, Version 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'Apache-2.0'},

    # MIT
    'MIT License': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'MIT'},
    'The MIT License': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'MIT'},
    'MIT': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'MIT'},

    # BSD variants
    'BSD License': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'BSD'},
    'BSD 3-Clause License': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'BSD-3-Clause'},
    'BSD-3-Clause': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'BSD-3-Clause'},
    'The BSD License': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'BSD'},
    'New BSD License': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'BSD-3-Clause'},
    'BSD-2-Clause': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'BSD-2-Clause'},
    'The BSD 2-Clause License': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'BSD-2-Clause'},
    'The BSD 3-Clause License': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'BSD-3-Clause'},
    'BSD License 3': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'BSD-3-Clause'},

    # Eclipse licenses
    'Eclipse Public License - v 1.0': {'gplv3_compatible': False, 'enterprise_safe': True, 'category': 'EPL-1.0'},
    'Eclipse Public License v1.0': {'gplv3_compatible': False, 'enterprise_safe': True, 'category': 'EPL-1.0'},
    'Eclipse Public License 1.0': {'gplv3_compatible': False, 'enterprise_safe': True, 'category': 'EPL-1.0'},
    'EPL-1.0': {'gplv3_compatible': False, 'enterprise_safe': True, 'category': 'EPL-1.0'},

    'Eclipse Public License - v 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'EPL-2.0'},
    'Eclipse Public License - Version 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'EPL-2.0'},
    'Eclipse Public License v2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'EPL-2.0'},
    'Eclipse Public License v. 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'EPL-2.0'},
    'Eclipse Public License 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'EPL-2.0'},
    'EPL 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'EPL-2.0'},
    'EPL-2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'EPL-2.0'},

    # EPL-2.0 with GPL Classpath Exception (dual license comune in Jakarta EE)
    'EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'EPL-2.0'},
    'Eclipse Public License v. 2.0 or GNU GPL v2+ with Classpath exception': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'EPL-2.0'},
    'EPL 2.0 OR GPL 2.0 WITH Classpath-exception-2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'EPL-2.0'},

    'Eclipse Distribution License - v 1.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'EDL-1.0'},
    'Eclipse Distribution License v1.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'EDL-1.0'},
    'EDL 1.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'EDL-1.0'},

    # LGPL - compatibile con GPLv3 e Enterprise
    'GNU Lesser General Public License': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'LGPL'},
    'Lesser General Public License, version 3 or greater': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'LGPL-3.0+'},
    'LGPL 2.1': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'LGPL-2.1'},
    'GNU Lesser General Public License, Version 2.1': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'LGPL-2.1'},
    'GNU LESSER GENERAL PUBLIC LICENSE, Version 2.1': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'LGPL-2.1'},
    'LGPL-2.1': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'LGPL-2.1'},
    'LGPL-2.1+': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'LGPL-2.1+'},
    'GNU Library General Public License v2.1 or later': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'LGPL-2.1+'},
    'GNU Lesser General Public License v2.1 or later': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'LGPL-2.1+'},

    # GPL
    'GNU General Public License, version 2': {'gplv3_compatible': False, 'enterprise_safe': False, 'category': 'GPL-2.0'},
    'GPL-2.0': {'gplv3_compatible': False, 'enterprise_safe': False, 'category': 'GPL-2.0'},
    'GNU General Public License v3.0': {'gplv3_compatible': True, 'enterprise_safe': False, 'category': 'GPL-3.0'},
    'GPL-3.0': {'gplv3_compatible': True, 'enterprise_safe': False, 'category': 'GPL-3.0'},

    # GPL v2 con FOSS Exception (usato da MySQL Connector/J) - enterprise safe per uso con FOSS
    'The GNU General Public License, v2 with Universal FOSS Exception, v1.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'GPL-2.0-with-FOSS-exception'},
    'GPLv2 with FOSS exception': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'GPL-2.0-with-FOSS-exception'},
    'GPL-2.0 with FOSS exception': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'GPL-2.0-with-FOSS-exception'},

    # GPL v2 con Classpath Exception - enterprise safe (linking exception)
    'GPL2 w/ CPE': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'GPL-2.0-with-CE'},
    'GPLv2 with Classpath Exception': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'GPL-2.0-with-CE'},
    'GNU General Public License, version 2 with the GNU Classpath Exception': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'GPL-2.0-with-CE'},
    'GPL-2.0-with-classpath-exception': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'GPL-2.0-with-CE'},

    # Mozilla Public License - compatibile
    'Mozilla Public License Version 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'MPL-2.0'},
    'Mozilla Public License, Version 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'MPL-2.0'},
    'MPL 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'MPL-2.0'},
    'MPL-2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'MPL-2.0'},

    # CDDL (all variants)
    'Common Development and Distribution License': {'gplv3_compatible': False, 'enterprise_safe': True, 'category': 'CDDL'},
    'CDDL-1.0': {'gplv3_compatible': False, 'enterprise_safe': True, 'category': 'CDDL-1.0'},
    'CDDL-1.1': {'gplv3_compatible': False, 'enterprise_safe': True, 'category': 'CDDL-1.1'},
    'CDDL 1.1': {'gplv3_compatible': False, 'enterprise_safe': True, 'category': 'CDDL-1.1'},
    'CDDL': {'gplv3_compatible': False, 'enterprise_safe': True, 'category': 'CDDL'},

    # CDDL with GPL dual licenses (can choose either license, so enterprise safe)
    'CDDL+GPL License': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'CDDL+GPL'},
    'CDDL/GPLv2+CE': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'CDDL/GPL-2.0-with-CE'},

    # Multi-license (Apache + CDDL + EPL) - used by Tomcat Servlet API
    'Apache License, Version 2.0 and Common Development And Distribution License (CDDL) Version 1.0 and Eclipse Public License - v 2.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'Apache-2.0+CDDL+EPL-2.0'},

    # Common Public License
    'CPL': {'gplv3_compatible': False, 'enterprise_safe': True, 'category': 'CPL'},
    'Common Public License Version 1.0': {'gplv3_compatible': False, 'enterprise_safe': True, 'category': 'CPL-1.0'},

    # ISC
    'ISC': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'ISC'},
    'ISC License': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'ISC'},

    # Bouncy Castle License - MIT-style permissive license
    'Bouncy Castle Licence': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'Bouncy-Castle'},
    'Bouncy Castle License': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'Bouncy-Castle'},

    # Public Domain / CC0
    'CC0-1.0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'CC0-1.0'},
    'Public Domain, per Creative Commons CC0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'CC0-1.0'},
    'CC0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'CC0-1.0'},

    # MIT-0 (MIT No Attribution)
    'MIT-0': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'MIT-0'},

    # CDDL + GPLv2 with classpath exception
    'CDDL + GPLv2 with classpath exception': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'CDDL/GPL-2.0-with-CE'},

    # Oracle Free Use Terms and Conditions (FUTC) - used by Oracle JDBC drivers
    'Oracle Free Use Terms and Conditions (FUTC)': {'gplv3_compatible': True, 'enterprise_safe': True, 'category': 'Oracle-FUTC'},
}

def normalize_license_name(license_name):
    """Normalize license name by removing extra whitespace and newlines"""
    if not license_name:
        return 'Unknown'
    # Replace newlines and multiple spaces with a single space
    normalized = ' '.join(license_name.split())
    return normalized

def infer_license_from_project_knowledge(group_id, artifact_id):
    """
    Infer license based on well-known groupId patterns when POM lacks license info.
    This is a fallback for artifacts from major projects with established licensing.
    Returns a license dict or None.
    """
    # Apache projects - these are well-known Apache artifacts with missing POM license info
    apache_artifacts = {
        'xalan:xalan': {'name': 'Apache License, Version 2.0', 'url': 'https://www.apache.org/licenses/LICENSE-2.0', 'note': 'Apache Xalan project'},
        'xalan:serializer': {'name': 'Apache License, Version 2.0', 'url': 'https://www.apache.org/licenses/LICENSE-2.0', 'note': 'Apache Xalan project'},
        'xerces:xercesImpl': {'name': 'Apache License, Version 2.0', 'url': 'https://www.apache.org/licenses/LICENSE-2.0', 'note': 'Apache Xerces project'},
        'xml-apis:xml-apis': {'name': 'Apache License, Version 2.0', 'url': 'https://www.apache.org/licenses/LICENSE-2.0', 'note': 'Apache XML Commons project'},
    }

    # Check exact match
    key = f"{group_id}:{artifact_id}"
    if key in apache_artifacts:
        lic_info = apache_artifacts[key]
        print(f"  ✓ License inferred from project knowledge: {lic_info['name']} ({lic_info['note']})")
        return [{
            'name': lic_info['name'],
            'url': lic_info['url'],
            'file': '',
            'source': 'inferred'
        }]

    # Check if it's an Apache groupId pattern
    if group_id.startswith('org.apache.') or group_id == 'apache':
        print(f"  ✓ License inferred from Apache groupId pattern: Apache License 2.0")
        return [{
            'name': 'Apache License, Version 2.0',
            'url': 'https://www.apache.org/licenses/LICENSE-2.0',
            'file': '',
            'source': 'inferred-pattern'
        }]

    return None

def fetch_license_from_maven_central(group_id, artifact_id, version):
    """
    Fetch license information from Maven Central POM
    Returns a list of license dictionaries or None if not found
    """
    try:
        # Convert groupId to path (e.g., org.apache.xalan -> org/apache/xalan)
        group_path = group_id.replace('.', '/')
        pom_url = f"https://repo1.maven.org/maven2/{group_path}/{artifact_id}/{version}/{artifact_id}-{version}.pom"

        print(f"  Fetching license info from Maven Central: {group_id}:{artifact_id}:{version}")

        with urllib.request.urlopen(pom_url, timeout=10) as response:
            pom_content = response.read()

        # Parse POM XML
        root = ET.fromstring(pom_content)

        # Handle namespaces
        namespaces = {'maven': 'http://maven.apache.org/POM/4.0.0'}

        # Try with namespace first
        licenses_elem = root.find('maven:licenses', namespaces)
        if licenses_elem is None:
            # Try without namespace
            licenses_elem = root.find('licenses')

        if licenses_elem is not None:
            licenses = []
            for lic in licenses_elem.findall('maven:license', namespaces) or licenses_elem.findall('license'):
                name_elem = lic.find('maven:name', namespaces) or lic.find('name')
                url_elem = lic.find('maven:url', namespaces) or lic.find('url')

                if name_elem is not None and name_elem.text:
                    licenses.append({
                        'name': normalize_license_name(name_elem.text),
                        'url': url_elem.text if url_elem is not None else '',
                        'file': '',
                        'source': 'maven-central'
                    })

            if licenses:
                print(f"  ✓ Found license from Maven Central: {licenses[0]['name']}")
                return licenses

        # If no licenses found in direct POM, try parent POM
        parent_elem = root.find('maven:parent', namespaces) or root.find('parent')
        if parent_elem is not None:
            parent_group = parent_elem.find('maven:groupId', namespaces) or parent_elem.find('groupId')
            parent_artifact = parent_elem.find('maven:artifactId', namespaces) or parent_elem.find('artifactId')
            parent_version = parent_elem.find('maven:version', namespaces) or parent_elem.find('version')

            if parent_group is not None and parent_artifact is not None and parent_version is not None:
                print(f"  → Checking parent POM: {parent_group.text}:{parent_artifact.text}")
                return fetch_license_from_maven_central(parent_group.text, parent_artifact.text, parent_version.text)

    except urllib.error.HTTPError as e:
        print(f"  ✗ HTTP error fetching POM: {e.code}")
    except urllib.error.URLError as e:
        print(f"  ✗ URL error fetching POM: {e.reason}")
    except ET.ParseError as e:
        print(f"  ✗ Error parsing POM XML: {e}")
    except Exception as e:
        print(f"  ✗ Unexpected error fetching license: {e}")

    return None

def parse_licenses_xml(xml_path):
    """Parse il file licenses.xml generato da Maven"""
    tree = ET.parse(xml_path)
    root = tree.getroot()
    dependencies = []

    for dep in root.findall('.//dependency'):
        group_id = dep.find('groupId').text if dep.find('groupId') is not None else 'unknown'
        artifact_id = dep.find('artifactId').text if dep.find('artifactId') is not None else 'unknown'
        version = dep.find('version').text if dep.find('version') is not None else 'unknown'

        licenses = []
        for lic in dep.findall('.//license'):
            lic_name = lic.find('name').text if lic.find('name') is not None else 'unknown'
            # Normalize the license name to handle multi-line strings
            lic_name = normalize_license_name(lic_name)
            lic_url = lic.find('url').text if lic.find('url') is not None else ''
            lic_file = lic.find('file').text if lic.find('file') is not None else ''
            licenses.append({'name': lic_name, 'url': lic_url, 'file': lic_file})

        dependencies.append({
            'groupId': group_id,
            'artifactId': artifact_id,
            'version': version,
            'licenses': licenses
        })

    return dependencies

def load_exceptions(exceptions_file=None):
    """Carica le eccezioni di licenza da file JSON"""
    exceptions = {}
    excluded_artifacts = set()

    if exceptions_file and Path(exceptions_file).exists():
        with open(exceptions_file, 'r') as f:
            data = json.load(f)
            for exc in data.get('exceptions', []):
                key = f"{exc['groupId']}:{exc.get('artifactId', '*')}"
                exceptions[key] = exc
                if exc.get('exclude_from_reports', False):
                    excluded_artifacts.add(key)
        print(f"Caricate {len(exceptions)} eccezioni dal file {exceptions_file}")
    else:
        print(f"File eccezioni non trovato: {exceptions_file}")

    return exceptions, excluded_artifacts

def matches_exception(group_id, artifact_id, exceptions):
    """Verifica se un artifact corrisponde a un'eccezione"""
    # Check exact match
    exact_key = f"{group_id}:{artifact_id}"
    if exact_key in exceptions:
        return exceptions[exact_key]

    # Check wildcard match
    wildcard_key = f"{group_id}:*"
    if wildcard_key in exceptions:
        return exceptions[wildcard_key]

    return None

def analyze_licenses(exceptions_file=None):
    """Analizza le licenze dal file XML generato da Maven"""
    xml_path = Path('target/generated-resources/licenses.xml')
    if not xml_path.exists():
        print(f"File {xml_path} non trovato!")
        return 1

    print(f"Parsing {xml_path}...")
    dependencies = parse_licenses_xml(xml_path)
    print(f"Trovate {len(dependencies)} dipendenze totali")

    # Carica eccezioni
    exceptions, excluded_artifacts = load_exceptions(exceptions_file)

    # Lista di artifact che sono tipicamente solo per test
    TEST_ARTIFACTS = {
        'junit:junit',
        'org.junit.jupiter:junit-jupiter',
        'org.junit.jupiter:junit-jupiter-api',
        'org.junit.jupiter:junit-jupiter-engine',
        'org.junit.jupiter:junit-jupiter-params',
        'org.junit.platform:junit-platform-commons',
        'org.junit.platform:junit-platform-engine',
        'org.mockito:mockito-core',
        'org.mockito:mockito-junit-jupiter',
        'org.hamcrest:hamcrest',
        'org.hamcrest:hamcrest-core',
        'org.xmlunit:xmlunit-core',
        'org.xmlunit:xmlunit-legacy',
        'org.xmlunit:xmlunit-placeholders',
        'org.assertj:assertj-core',
        'org.springframework.boot:spring-boot-starter-test',
        'org.springframework.security:spring-security-test',
        'org.springframework.ws:spring-ws-test'
    }

    # Filtra le dipendenze test e quelle escluse dalle eccezioni
    filtered_dependencies = []
    excluded_count = 0
    excluded_by_exception = 0

    for dep in dependencies:
        artifact_key = f"{dep['groupId']}:{dep['artifactId']}"

        # Check if excluded by exception
        exception = matches_exception(dep['groupId'], dep['artifactId'], exceptions)
        if exception and exception.get('exclude_from_reports', False):
            excluded_by_exception += 1
            print(f"  Escluso per eccezione: {artifact_key} - {exception['reason']}")
            continue

        # Check if test artifact
        if artifact_key in TEST_ARTIFACTS:
            excluded_count += 1
            print(f"  Escluso (test scope): {artifact_key}")
            continue

        filtered_dependencies.append(dep)

    dependencies = filtered_dependencies
    print(f"Dipendenze dopo esclusioni: {len(dependencies)} ({excluded_count} test, {excluded_by_exception} per eccezione)")

    results = []
    license_counts = defaultdict(int)
    gplv3_issues = []
    enterprise_issues = []
    artifacts_by_license = defaultdict(list)

    # Crea directory output
    output_dir = Path('third-party-licenses')
    output_dir.mkdir(exist_ok=True)
    licenses_dir = Path('target/generated-resources/licenses')

    for dep in dependencies:
        group_id = dep['groupId']
        artifact_id = dep['artifactId']
        version = dep['version']

        # Gestione dual/multiple licensing - scegli la licenza più compatibile
        best_license = None
        best_compatibility = None
        best_score = -1
        all_license_names = []

        for lic in dep['licenses']:
            lic_name = lic.get('name', 'Unknown')
            all_license_names.append(lic_name)

            # Ottieni compatibilità per questa licenza
            compat = LICENSE_COMPATIBILITY.get(lic_name, {
                'gplv3_compatible': None,
                'enterprise_safe': None,
                'category': lic_name
            })

            # Calcola score di compatibilità (priorità: GPLv3 + Enterprise > solo Enterprise > solo GPLv3 > nessuno)
            score = 0
            if compat['gplv3_compatible'] == True:
                score += 2
            if compat['enterprise_safe'] == True:
                score += 1

            # Scegli la licenza con score migliore
            if score > best_score:
                best_score = score
                best_license = lic
                best_compatibility = compat

        # Se non ci sono licenze, prova a recuperarle da Maven Central
        if best_license is None or best_license.get('name') == 'Unknown':
            print(f"  ⚠️ No license found for {group_id}:{artifact_id}, attempting to fetch from Maven Central...")
            fetched_licenses = fetch_license_from_maven_central(group_id, artifact_id, version)

            # If Maven Central didn't have it, try inferring from project knowledge
            if not fetched_licenses:
                print(f"  → Attempting to infer license from project knowledge...")
                fetched_licenses = infer_license_from_project_knowledge(group_id, artifact_id)

            if fetched_licenses:
                # Aggiungi le licenze recuperate alla lista delle licenze della dipendenza
                dep['licenses'] = fetched_licenses
                all_license_names = []

                # Ri-esegui il processo di selezione della migliore licenza
                for lic in fetched_licenses:
                    lic_name = lic.get('name', 'Unknown')
                    all_license_names.append(lic_name)

                    # Ottieni compatibilità per questa licenza
                    compat = LICENSE_COMPATIBILITY.get(lic_name, {
                        'gplv3_compatible': None,
                        'enterprise_safe': None,
                        'category': lic_name
                    })

                    # Calcola score di compatibilità
                    score = 0
                    if compat['gplv3_compatible'] == True:
                        score += 2
                    if compat['enterprise_safe'] == True:
                        score += 1

                    # Scegli la licenza con score migliore
                    if score > best_score:
                        best_score = score
                        best_license = lic
                        best_compatibility = compat

            # Se ancora non abbiamo una licenza, usa Unknown
            if best_license is None or best_license.get('name') == 'Unknown':
                best_license = {'name': 'Unknown', 'url': '', 'file': ''}
                best_compatibility = {
                    'gplv3_compatible': None,
                    'enterprise_safe': None,
                    'category': 'Unknown'
                }
                all_license_names = ['Unknown']
                print(f"  ✗ Could not determine license for {group_id}:{artifact_id}")

        license_name = best_license['name']
        license_file = best_license.get('file', '')
        compatibility = best_compatibility

        # Se c'è dual licensing, mostra tutte le licenze nel nome
        if len(all_license_names) > 1:
            display_license = ' OR '.join(all_license_names)
            print(f"  Dual license per {group_id}:{artifact_id}: {display_license} -> Usando: {license_name}")
        else:
            display_license = license_name

        license_category = compatibility['category']
        license_counts[license_category] += 1
        artifacts_by_license[license_category].append(f"{group_id}:{artifact_id}:{version}")

        # Organizza per groupId/artifactId
        group_dir = output_dir / group_id.replace('.', '/')
        artifact_dir = group_dir / artifact_id
        artifact_dir.mkdir(parents=True, exist_ok=True)

        # Copia il file di licenza se esiste
        if license_file and licenses_dir.exists():
            license_source = licenses_dir / license_file
            if license_source.exists():
                target_license = artifact_dir / 'LICENSE.txt'
                try:
                    target_license.write_text(license_source.read_text(encoding='utf-8', errors='ignore'))
                except Exception as e:
                    print(f"Errore copiando licenza per {group_id}:{artifact_id}: {e}")

        # Salva metadata
        metadata = {
            'groupId': group_id,
            'artifactId': artifact_id,
            'version': version,
            'license': license_name,
            'license_category': license_category,
            'gplv3_compatible': compatibility['gplv3_compatible'],
            'enterprise_safe': compatibility['enterprise_safe'],
            'all_licenses': dep['licenses']
        }

        metadata_file = artifact_dir / 'metadata.json'
        with open(metadata_file, 'w') as f:
            json.dump(metadata, f, indent=2)

        # Check se c'è un'eccezione per questo artifact
        artifact_key = f"{group_id}:{artifact_id}"
        exception = matches_exception(group_id, artifact_id, exceptions)

        result = {
            'groupId': group_id,
            'artifactId': artifact_id,
            'version': version,
            'license': display_license,  # Mostra tutte le licenze se dual licensing
            'license_category': license_category,
            'gplv3_compatible': compatibility['gplv3_compatible'],
            'enterprise_safe': compatibility['enterprise_safe'],
            'has_exception': exception is not None and not exception.get('exclude_from_reports', False),
            'exception_reason': exception['reason'] if exception and not exception.get('exclude_from_reports', False) else None
        }
        results.append(result)

        # Identifica problemi di compatibilità
        # (exception già definito sopra)

        gplv3_compat = compatibility['gplv3_compatible']
        enterprise_safe = compatibility['enterprise_safe']

        # Se entrambi sono None, conta come un unico problema "unknown"
        if gplv3_compat is None and enterprise_safe is None:
            if exception:
                print(f"  ⚠️ Eccezione gestita: {artifact_key} - {license_name} (licenza sconosciuta/non classificata) - Motivo: {exception['reason']}")
            else:
                gplv3_issues.append(f"{group_id}:{artifact_id} - {license_name} (licenza sconosciuta/non classificata)")
        else:
            # Gestisci problemi GPLv3
            if gplv3_compat is False or gplv3_compat is None:
                if exception:
                    reason_text = "GPLv3 incompatibile" if gplv3_compat is False else "licenza non classificata per GPLv3"
                    print(f"  ⚠️ Eccezione gestita: {artifact_key} - {license_name} ({reason_text}) - Motivo: {exception['reason']}")
                else:
                    reason_text = "incompatibile con GPLv3" if gplv3_compat is False else "licenza non classificata per GPLv3"
                    gplv3_issues.append(f"{group_id}:{artifact_id} - {license_name} ({reason_text})")

            # Gestisci problemi Enterprise
            if enterprise_safe is False or enterprise_safe is None:
                if exception:
                    reason_text = "enterprise unsafe" if enterprise_safe is False else "licenza non classificata per Enterprise"
                    print(f"  ⚠️ Eccezione gestita: {artifact_key} - {license_name} ({reason_text}) - Motivo: {exception['reason']}")
                else:
                    reason_text = "problematico per uso enterprise" if enterprise_safe is False else "licenza non classificata per Enterprise"
                    enterprise_issues.append(f"{group_id}:{artifact_id} - {license_name} ({reason_text})")

    # Combina tutti i problemi per il report (mantenendo retrocompatibilità)
    all_compatibility_issues = gplv3_issues + enterprise_issues

    # Genera report JSON nella directory third-party-licenses
    report_data = {
        'artifacts': results,
        'summary': {
            'total_artifacts': len(results),
            'license_counts': dict(license_counts),
            'artifacts_by_license': dict(artifacts_by_license),
            'compatibility_issues': all_compatibility_issues
        }
    }

    with open(output_dir / 'license-summary.json', 'w') as f:
        json.dump(report_data, f, indent=2)

    # Genera CSV per Excel
    generate_csv_report(results, output_dir)
    # Genera report HTML
    generate_html_report(report_data, output_dir)
    # Genera report Markdown
    generate_markdown_report(report_data, output_dir)

    total_compatibility_issues = len(gplv3_issues) + len(enterprise_issues)

    print(f"\nAnalisi completata:")
    print(f"  - Artifacts processati: {len(results)}")
    print(f"  - Tipi di licenza trovati: {len(license_counts)}")
    print(f"  - Problemi di compatibilità GPLv3 NON gestiti: {len(gplv3_issues)}")
    print(f"  - Problemi di compatibilità Enterprise NON gestiti: {len(enterprise_issues)}")
    print(f"  - Report salvati in: {output_dir}/")

    # Restituisci exit code 1 se ci sono problemi non gestiti
    if total_compatibility_issues == 0:
        print("\n✅ Tutte le licenze sono compatibili o gestite tramite eccezioni")
        return 0
    else:
        error_messages = []
        if len(gplv3_issues) > 0:
            error_messages.append(f"{len(gplv3_issues)} problemi di compatibilità GPLv3")
        if len(enterprise_issues) > 0:
            error_messages.append(f"{len(enterprise_issues)} problemi di compatibilità Enterprise (ShareAlike/Copyleft)")

        print(f"\n❌ Trovati: {' e '.join(error_messages)}")

        if len(gplv3_issues) > 0:
            print(f"\nProblemi GPLv3:")
            for issue in gplv3_issues:
                print(f"  - {issue}")

        if len(enterprise_issues) > 0:
            print(f"\nProblemi Enterprise:")
            for issue in enterprise_issues:
                print(f"  - {issue}")

        return 1

def generate_csv_report(artifacts, output_dir):
    """Genera un report CSV con la mappatura artifact → licenza"""
    csv_path = output_dir / 'license-artifacts-mapping.csv'
    with open(csv_path, 'w', newline='', encoding='utf-8') as csvfile:
        fieldnames = ['GroupId', 'ArtifactId', 'Version', 'License', 'License Category', 'GPLv3 Compatible', 'Enterprise Safe', 'Exception', 'Exception Reason']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()
        for artifact in artifacts:
            writer.writerow({
                'GroupId': artifact['groupId'],
                'ArtifactId': artifact['artifactId'],
                'Version': artifact['version'],
                'License': artifact['license'],
                'License Category': artifact['license_category'],
                'GPLv3 Compatible': 'Yes' if artifact['gplv3_compatible'] else ('No' if artifact['gplv3_compatible'] is False else 'Unknown'),
                'Enterprise Safe': 'Yes' if artifact['enterprise_safe'] else ('No' if artifact['enterprise_safe'] is False else 'Unknown'),
                'Exception': 'Yes' if artifact.get('has_exception', False) else '',
                'Exception Reason': artifact.get('exception_reason', '') or ''
            })

def generate_html_report(data, output_dir):
    """Genera report HTML migliorato"""
    from collections import defaultdict
    import html as html_module

    artifacts_by_group = defaultdict(list)
    for artifact in data['artifacts']:
        artifacts_by_group[artifact['groupId']].append(artifact)

    html = f"""<!DOCTYPE html>
<html>
<head>
    <title>License Analysis Report</title>
    <style>
        body {{ font-family: 'Segoe UI', Arial, sans-serif; margin: 20px; background: #f5f5f5; }}
        .container {{ max-width: 1600px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }}
        h1 {{ color: #333; border-bottom: 3px solid #007bff; padding-bottom: 10px; }}
        .summary {{ background: #e8f4ff; padding: 15px; border-radius: 5px; margin-bottom: 20px; border-left: 4px solid #007bff; }}
        .summary-grid {{ display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px; margin-top: 10px; }}
        .summary-card {{ background: white; padding: 10px; border-radius: 4px; text-align: center; }}
        .summary-card .number {{ font-size: 2em; font-weight: bold; color: #007bff; }}
        .summary-card .label {{ color: #666; font-size: 0.9em; }}
        .issues {{ background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; border-radius: 5px; }}
        .issues h3 {{ margin-top: 0; color: #856404; }}
        .issues ul {{ margin: 10px 0; }}
        table {{ border-collapse: collapse; width: 100%; margin-top: 10px; table-layout: fixed; }}
        th, td {{ border: 1px solid #ddd; padding: 10px; text-align: left; }}
        th {{ background-color: #007bff; color: white; position: sticky; top: 0; z-index: 10; }}
        tr:nth-child(even) {{ background-color: #f9f9f9; }}
        tr:hover {{ background-color: #f0f8ff; }}
        .compatible {{ color: #28a745; font-weight: bold; }}
        .incompatible {{ color: #dc3545; font-weight: bold; }}
        .unknown {{ color: #ffc107; font-weight: bold; }}
        .group-header {{ background: #f8f9fa; font-weight: bold; border-left: 3px solid #007bff; }}

        /* License cell styling */
        .license-cell {{
            max-width: 500px;
            position: relative;
            cursor: pointer;
            word-break: break-word;
        }}
        .license-text {{
            display: block;
            max-height: 3em;
            overflow: hidden;
            transition: max-height 0.3s ease;
        }}
        .license-cell:hover .license-text,
        .license-cell.expanded .license-text {{
            max-height: none;
            background: #fffbf0;
            padding: 5px;
            border-radius: 3px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            position: relative;
            z-index: 5;
        }}
        .copy-button {{
            display: none;
            position: absolute;
            right: 5px;
            top: 5px;
            padding: 3px 8px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            font-size: 12px;
            z-index: 10;
        }}
        .license-cell:hover .copy-button,
        .license-cell.expanded .copy-button {{
            display: inline-block;
        }}
        .copy-button:hover {{
            background: #0056b3;
        }}
        .expand-hint {{
            font-size: 11px;
            color: #666;
            font-style: italic;
            display: inline;
            margin-left: 5px;
        }}

        /* Exception cell styling */
        .exception-cell {{
            max-width: 300px;
            position: relative;
            cursor: pointer;
            word-break: break-word;
        }}
        .exception-text {{
            display: block;
            max-height: 3em;
            overflow: hidden;
            transition: max-height 0.3s ease;
        }}
        .exception-cell:hover .exception-text,
        .exception-cell.expanded .exception-text {{
            max-height: none;
            background: #fff9e6;
            padding: 5px;
            border-radius: 3px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            position: relative;
            z-index: 5;
        }}
        .exception-copy-button {{
            display: none;
            position: absolute;
            right: 5px;
            top: 5px;
            padding: 3px 8px;
            background: #856404;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            font-size: 12px;
            z-index: 10;
        }}
        .exception-cell:hover .exception-copy-button,
        .exception-cell.expanded .exception-copy-button {{
            display: inline-block;
        }}
        .exception-copy-button:hover {{
            background: #6c5229;
        }}

        /* Column widths */
        th:nth-child(1), td:nth-child(1) {{ width: 18%; }}
        th:nth-child(2), td:nth-child(2) {{ width: 18%; }}
        th:nth-child(3), td:nth-child(3) {{ width: 8%; }}
        th:nth-child(4), td:nth-child(4) {{ width: 30%; }}
        th:nth-child(5), td:nth-child(5) {{ width: 6%; }}
        th:nth-child(6), td:nth-child(6) {{ width: 6%; }}
        th:nth-child(7), td:nth-child(7) {{ width: 14%; }}

        /* Exception styling */
        .exception-badge {{
            background: #fff3cd;
            color: #856404;
            padding: 2px 6px;
            border-radius: 3px;
            font-size: 11px;
            font-weight: bold;
            display: inline-block;
            margin-top: 3px;
        }}
        tr.has-exception {{
            background-color: #fff9e6 !important;
        }}
        tr.has-exception:hover {{
            background-color: #fff3cd !important;
        }}
    </style>
    <script>
        function copyToClipboard(text) {{
            navigator.clipboard.writeText(text).then(function() {{
                // Show temporary success message
                event.target.textContent = 'Copied!';
                setTimeout(function() {{
                    event.target.textContent = 'Copy';
                }}, 1500);
            }});
        }}

        function toggleExpand(element) {{
            element.classList.toggle('expanded');
        }}

        document.addEventListener('DOMContentLoaded', function() {{
            // Add click handlers to license cells
            document.querySelectorAll('.license-cell').forEach(function(cell) {{
                cell.addEventListener('click', function(e) {{
                    if (!e.target.classList.contains('copy-button')) {{
                        toggleExpand(this);
                    }}
                }});
            }});

            // Add click handlers to exception cells
            document.querySelectorAll('.exception-cell').forEach(function(cell) {{
                cell.addEventListener('click', function(e) {{
                    if (!e.target.classList.contains('exception-copy-button')) {{
                        toggleExpand(this);
                    }}
                }});
            }});
        }});
    </script>
</head>
<body>
    <div class="container">
        <h1>Third-party License Analysis Report</h1>
        <div class="summary">
            <h2>Summary</h2>
            <div class="summary-grid">
                <div class="summary-card">
                    <div class="number">{data['summary']['total_artifacts']}</div>
                    <div class="label">Total Artifacts</div>
                </div>
                <div class="summary-card">
                    <div class="number">{len(data['summary']['license_counts'])}</div>
                    <div class="label">License Types</div>
                </div>
                <div class="summary-card">
                    <div class="number">{len(data['summary']['compatibility_issues'])}</div>
                    <div class="label">Issues</div>
                </div>
            </div>
        </div>
"""

    if data['summary']['compatibility_issues']:
        html += """
        <div class="issues">
            <h3>⚠️ Compatibility Issues</h3>
            <ul>
"""
        for issue in data['summary']['compatibility_issues']:
            html += f"                <li>{html_module.escape(issue)}</li>\n"
        html += """            </ul>
        </div>
"""

    html += """
        <h2>Complete Artifact Listing</h2>
        <p style="font-size: 14px; color: #666; margin-bottom: 10px;">
            💡 Hover over license or exception cells to view full text. Use Copy button to copy the content.<br>
            ⚠️ Artifacts with yellow background have managed exceptions.
        </p>
        <table>
            <tr><th>Group ID</th><th>Artifact ID</th><th>Version</th><th>License</th><th>GPLv3</th><th>Enterprise</th><th>Exception</th></tr>
"""

    for group_id in sorted(artifacts_by_group.keys()):
        artifacts = sorted(artifacts_by_group[group_id], key=lambda x: x['artifactId'])
        html += f'<tr class="group-header"><td colspan="7">{html_module.escape(group_id)} ({len(artifacts)} artifacts)</td></tr>'
        for artifact in artifacts:
            gplv3_class = 'compatible' if artifact['gplv3_compatible'] else ('incompatible' if artifact['gplv3_compatible'] is False else 'unknown')
            enterprise_class = 'compatible' if artifact['enterprise_safe'] else ('incompatible' if artifact['enterprise_safe'] is False else 'unknown')
            has_exception = artifact.get('has_exception', False)
            exception_reason = artifact.get('exception_reason', '')
            row_class = 'has-exception' if has_exception else ''

            license_text = artifact['license']
            escaped_license = html_module.escape(license_text)

            exception_cell = ''
            if has_exception:
                escaped_reason = html_module.escape(exception_reason)
                exception_cell = f'''<div class="exception-cell">
                    <span class="exception-badge">EXCEPTION</span><br>
                    <span class="exception-text">{escaped_reason}</span>
                    <button class="exception-copy-button" onclick="copyToClipboard('{escaped_reason.replace("'", "\\'")}')">Copy</button>
                </div>'''

            html += f"""<tr class="{row_class}">
                <td>{html_module.escape(artifact['groupId'])}</td>
                <td><strong>{html_module.escape(artifact['artifactId'])}</strong></td>
                <td>{html_module.escape(artifact['version'])}</td>
                <td class="license-cell">
                    <span class="license-text">{escaped_license}</span>
                    <button class="copy-button" onclick="copyToClipboard('{escaped_license.replace("'", "\\'")}')">Copy</button>
                </td>
                <td class="{gplv3_class}">{"✅" if artifact['gplv3_compatible'] else ("❌" if artifact['gplv3_compatible'] is False else "❓")}</td>
                <td class="{enterprise_class}">{"✅" if artifact['enterprise_safe'] else ("❌" if artifact['enterprise_safe'] is False else "❓")}</td>
                <td>{exception_cell}</td>
            </tr>"""

    html += """        </table>
    </div>
</body>
</html>"""

    with open(output_dir / 'license-compatibility-report.html', 'w', encoding='utf-8') as f:
        f.write(html)

def generate_markdown_report(data, output_dir):
    """Genera report Markdown migliorato"""
    md = "# 📄 License Compatibility Report\n\n"

    # Conta le eccezioni
    exceptions_count = sum(1 for a in data['artifacts'] if a.get('has_exception', False))

    md += f"## 📊 Summary\n"
    md += f"- **Total Artifacts:** {data['summary']['total_artifacts']}\n"
    md += f"- **License Types:** {len(data['summary']['license_counts'])}\n"
    md += f"- **Compatibility Issues:** {len(data['summary']['compatibility_issues'])}\n"
    md += f"- **Managed Exceptions:** {exceptions_count}\n\n"

    md += "## 📈 License Distribution\n\n"
    md += "| License Type | Count | Percentage |\n"
    md += "|-------------|-------|------------|\n"

    total = data['summary']['total_artifacts']
    for license_type, count in sorted(data['summary']['license_counts'].items(), key=lambda x: x[1], reverse=True):
        percentage = (count / total * 100) if total > 0 else 0
        md += f"| {license_type} | {count} | {percentage:.1f}% |\n"

    md += "\n## ⚠️ Compatibility Issues\n"
    if data['summary']['compatibility_issues']:
        for issue in data['summary']['compatibility_issues']:
            md += f"- ❌ {issue}\n"
    else:
        md += "✅ No compatibility issues detected\n"

    # Aggiungi sezione per le eccezioni gestite
    exceptions = [a for a in data['artifacts'] if a.get('has_exception', False)]
    if exceptions:
        md += "\n## 🔶 Managed Exceptions\n\n"
        md += "The following artifacts have known compatibility issues but are managed through exceptions:\n\n"
        for artifact in exceptions:
            md += f"- **{artifact['groupId']}:{artifact['artifactId']}** ({artifact['license_category']})\n"
            md += f"  - Reason: {artifact['exception_reason']}\n"

    md += "\n## 📋 Artifacts by License Type\n\n"
    for license_type, artifacts in sorted(data['summary']['artifacts_by_license'].items()):
        md += f"\n### {license_type} ({len(artifacts)} artifacts)\n"
        if len(artifacts) <= 5:
            for artifact in artifacts:
                md += f"- {artifact}\n"
        else:
            for artifact in artifacts[:5]:
                md += f"- {artifact}\n"
            md += f"- ... and {len(artifacts) - 5} more\n"

    # Compatibility analysis
    compatible_both = sum(1 for a in data['artifacts'] if a['gplv3_compatible'] and a['enterprise_safe'])
    gplv3_issue_only = sum(1 for a in data['artifacts'] if not a['gplv3_compatible'] and a['enterprise_safe'])
    enterprise_issue_only = sum(1 for a in data['artifacts'] if a['gplv3_compatible'] and not a['enterprise_safe'])
    both_issues = sum(1 for a in data['artifacts'] if a['gplv3_compatible'] is False and a['enterprise_safe'] is False)
    unknown = sum(1 for a in data['artifacts'] if a['gplv3_compatible'] is None or a['enterprise_safe'] is None)

    md += "\n## 🔍 Compatibility Analysis\n\n"
    md += "| Status | GPLv3 Compatible | Enterprise Safe | Count |\n"
    md += "|--------|------------------|-----------------|-------|\n"
    md += f"| ✅ Compatible with both | Yes | Yes | {compatible_both} |\n"
    if gplv3_issue_only > 0:
        md += f"| ⚠️ GPLv3 issue only | No | Yes | {gplv3_issue_only} |\n"
    if enterprise_issue_only > 0:
        md += f"| ⚠️ Enterprise issue only | Yes | No | {enterprise_issue_only} |\n"
    if both_issues > 0:
        md += f"| ❌ Both issues | No | No | {both_issues} |\n"
    if unknown > 0:
        md += f"| ❓ Unknown | Unknown | Unknown | {unknown} |\n"

    md += f"\n---\n*Report generated for {data['summary']['total_artifacts']} dependencies*\n"

    with open(output_dir / 'license-compatibility-report.md', 'w', encoding='utf-8') as f:
        f.write(md)

if __name__ == '__main__':
    import argparse
    parser = argparse.ArgumentParser(description='Analizza le licenze delle dipendenze Maven')
    parser.add_argument('--exceptions', help='File JSON con le eccezioni di licenza', default='license-exceptions.json')
    args = parser.parse_args()

    exit_code = analyze_licenses(exceptions_file=args.exceptions)
    sys.exit(exit_code if exit_code else 0)