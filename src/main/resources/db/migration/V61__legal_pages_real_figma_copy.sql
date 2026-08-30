-- ==============================================================================
-- Viajes Carolina — Migration V61: corrige el copy de las 5 páginas legales
-- ==============================================================================
-- V59 sembró las 5 tablas con copy provisional (el agente que la escribió no
-- tuvo acceso a las herramientas de Figma y redactó contenido propio para
-- Privacidad/Cookies/MINCETUR, contradiciendo la decisión explícita del
-- usuario de usar el texto real de Figma como definitivo). Esta migración
-- reemplaza el contenido de las 5 filas (id=1) por el texto literal extraído
-- de los 5 node-ids de Figma (690:4, 690:189, 690:585, 690:810, 690:390).
-- ==============================================================================

-- ------------------------------------------------------------------------------
-- 1. legal_terms — Términos y condiciones
-- ------------------------------------------------------------------------------
UPDATE legal_terms SET
    eyebrow = 'CENTRO LEGAL / TÉRMINOS Y CONDICIONES',
    title = 'Términos y condiciones',
    introduction = 'El presente documento establece las condiciones generales aplicables a la orientación, cotización y gestión de servicios turísticos realizados por Viajes Carolina.',
    document_control_label = 'CONTROL DOCUMENTAL',
    document_control_text = 'DOCUMENTO: TÉRMINOS Y CONDICIONES · CÓDIGO: VC-LEG-01 · VERSIÓN: 1.0 · VIGENCIA: AGOSTO 2026 · RESPONSABLE: VIAJES CAROLINA',
    sections_json = '[
        {"title":"Alcance de nuestros servicios","body":"Viajes Carolina brinda orientación, cotización, intermediación y gestión de servicios turísticos según el alcance indicado en cada propuesta. Solo forman parte del servicio los vuelos, alojamientos, traslados, actividades, seguros u otros conceptos descritos expresamente. Los servicios son prestados directamente por los proveedores identificados en la documentación de viaje."},
        {"title":"Cotizaciones y disponibilidad","body":"Las cotizaciones son informativas y tienen la vigencia indicada en la propuesta. Tarifas, impuestos, tipo de cambio, cupos, horarios y condiciones pueden modificarse hasta que la reserva y el pago sean confirmados. Cualquier variación será comunicada antes de solicitar la aceptación del cliente."},
        {"title":"Reservas y pagos","body":"La reserva queda confirmada cuando el cliente acepta la propuesta, entrega los datos requeridos y realiza el pago acordado. El cliente debe revisar nombres, fechas, destinos e inclusiones antes de autorizar la emisión. Los comprobantes y condiciones particulares se entregan por los canales acordados."},
        {"title":"Cambios, cancelaciones y reembolsos","body":"Los cambios, cancelaciones, no presentación y reembolsos se sujetan a las reglas de cada aerolínea, hotel u operador. Pueden existir penalidades, diferencias tarifarias o servicios no reembolsables. Antes del pago, Viajes Carolina informará las restricciones relevantes disponibles."},
        {"title":"Responsabilidades del viajero","body":"El viajero debe proporcionar información correcta, revisar la vigencia de documentos y cumplir requisitos migratorios, sanitarios, de equipaje y de ingreso al destino. También debe presentarse dentro de los horarios informados y comunicar oportunamente cualquier condición especial que afecte el viaje."},
        {"title":"Atención y contacto","body":"Las consultas y solicitudes se atienden por los canales oficiales publicados en esta web. Antes de confirmar una reserva, el cliente puede solicitar aclaraciones sobre precios, inclusiones, restricciones o responsabilidades. Las condiciones específicas de cada propuesta prevalecen sobre la información general de esta página."}
    ]'::jsonb,
    closing_title = 'Consulta previa a la aceptación',
    closing_body = 'Antes de confirmar una reserva, el cliente puede solicitar aclaraciones sobre precios, inclusiones, restricciones, responsabilidades y condiciones particulares. La aceptación debe realizarse únicamente después de comprender el alcance de la propuesta.',
    closing_link_label = 'Acceder al canal de consultas'
WHERE id = 1;

-- ------------------------------------------------------------------------------
-- 2. legal_privacy — Política de privacidad
-- ------------------------------------------------------------------------------
UPDATE legal_privacy SET
    eyebrow = 'CENTRO LEGAL / POLÍTICA DE PRIVACIDAD',
    title = 'Política de privacidad',
    introduction = 'El presente documento describe el tratamiento de los datos personales recibidos por Viajes Carolina, las finalidades aplicables y los mecanismos disponibles para el ejercicio de derechos.',
    document_control_label = 'CONTROL DOCUMENTAL',
    document_control_text = 'DOCUMENTO: POLÍTICA DE PRIVACIDAD · CÓDIGO: VC-LEG-02 · VERSIÓN: 1.0 · VIGENCIA: AGOSTO 2026 · RESPONSABLE: VIAJES CAROLINA',
    sections_json = '[
        {"title":"Quién es responsable","body":"Viajes Carolina es responsable del tratamiento de los datos personales recibidos mediante la web, WhatsApp, correo u otros canales oficiales. La atención se realiza conforme a las finalidades informadas y a la normativa peruana aplicable de protección de datos personales."},
        {"title":"Qué datos podemos recopilar","body":"Podemos solicitar nombres, documento de identidad, fecha de nacimiento, datos de contacto e información necesaria para cotizar o gestionar servicios turísticos. También tratamos los datos que compartas voluntariamente y los estrictamente requeridos por aerolíneas, hoteles u operadores."},
        {"title":"Para qué utilizamos la información","body":"Usamos la información para responder consultas, preparar cotizaciones, gestionar reservas y pagos, emitir documentación, brindar asistencia y cumplir obligaciones legales o contractuales. No utilizamos los datos para finalidades incompatibles sin informar o solicitar la autorización correspondiente."},
        {"title":"Con quién puede compartirse","body":"Compartimos únicamente la información necesaria con aerolíneas, alojamientos, operadores, aseguradoras, procesadores de pago u otros proveedores vinculados al servicio. También podemos comunicarla a autoridades cuando exista obligación legal. No vendemos bases de datos personales."},
        {"title":"Conservación y seguridad","body":"Conservamos la información durante el tiempo necesario para atender la finalidad, las obligaciones legales y eventuales reclamos. Aplicamos controles razonables de acceso, almacenamiento y confidencialidad; ningún sistema ofrece seguridad absoluta, por lo que revisamos periódicamente nuestras medidas."},
        {"title":"Tus derechos","body":"Puedes ejercer los derechos de acceso, rectificación, cancelación y oposición, así como revocar el consentimiento cuando corresponda. La solicitud debe permitir identificar al titular y describir el pedido; responderemos dentro de los plazos aplicables."},
        {"title":"Contacto sobre privacidad","body":"Para consultas o solicitudes relacionadas con privacidad, escribe a contacto@viajescarolina.com indicando el asunto «Protección de datos». Podemos solicitar información adicional para verificar tu identidad antes de entregar o modificar datos."}
    ]'::jsonb,
    closing_title = 'Ejercicio de derechos sobre datos personales',
    closing_body = 'El titular puede solicitar información sobre el tratamiento de sus datos personales y ejercer los derechos de acceso, rectificación, cancelación u oposición mediante los canales oficiales. La atención estará sujeta a la verificación de identidad y a los plazos aplicables.',
    closing_link_label = 'Acceder al canal de privacidad'
WHERE id = 1;

-- ------------------------------------------------------------------------------
-- 3. legal_cookies — Política de cookies
-- ------------------------------------------------------------------------------
UPDATE legal_cookies SET
    eyebrow = 'CENTRO LEGAL / POLÍTICA DE COOKIES',
    title = 'Política de cookies',
    introduction = 'El presente documento informa sobre el uso de cookies y tecnologías similares, sus finalidades y las opciones disponibles para administrar el consentimiento.',
    document_control_label = 'CONTROL DOCUMENTAL',
    document_control_text = 'DOCUMENTO: POLÍTICA DE COOKIES · CÓDIGO: VC-LEG-03 · VERSIÓN: 1.0 · VIGENCIA: AGOSTO 2026 · RESPONSABLE: VIAJES CAROLINA',
    sections_json = '[
        {"title":"Qué son las cookies","body":"Las cookies son pequeños archivos que el navegador almacena para recordar información técnica, mantener funciones de navegación o conservar preferencias. También pueden utilizarse tecnologías similares, como almacenamiento local o identificadores de sesión."},
        {"title":"Cookies necesarias","body":"Las cookies necesarias permiten seguridad, funcionamiento básico, gestión de sesión y conservación de la elección de privacidad. Se activan porque el sitio no podría operar correctamente sin ellas y no se utilizan para publicidad personalizada."},
        {"title":"Cookies de analítica","body":"Las cookies de analítica permiten conocer de forma agregada qué secciones se visitan, posibles errores y rendimiento del sitio. Solo se activan con tu consentimiento y procuramos configurar la medición para reducir la identificación individual."},
        {"title":"Cookies de preferencias","body":"Las cookies de preferencias recuerdan decisiones como idioma, región o configuración del sitio cuando estas funciones estén disponibles. Su rechazo puede hacer que algunas preferencias deban seleccionarse nuevamente."},
        {"title":"Cómo cambiar tu decisión","body":"Puedes aceptar, rechazar o actualizar las categorías no esenciales desde «Configurar cookies» en el footer. El retiro del consentimiento no afecta el tratamiento realizado previamente y la nueva elección se aplicará en visitas posteriores."},
        {"title":"Cookies de terceros","body":"Mapas, videos, redes sociales u otros contenidos externos pueden instalar cookies o solicitar datos antes de cargarse. Estos proveedores aplican sus propias políticas. Cuando sea posible, bloqueamos el contenido no esencial hasta obtener tu autorización."}
    ]'::jsonb,
    closing_title = 'Gestión del consentimiento',
    closing_body = 'El consentimiento para categorías no esenciales puede otorgarse, rechazarse o modificarse en cualquier momento. La nueva elección se aplicará en las visitas posteriores sin afectar el tratamiento realizado previamente.',
    closing_link_label = 'Configurar preferencias',
    cookie_categories_json = '[
        {"key":"essential","name":"Esenciales","description":"Necesarias para seguridad, funcionamiento básico y gestión de sesión. No pueden desactivarse.","required":true},
        {"key":"analytics","name":"Analítica","description":"Nos permiten conocer de forma agregada qué secciones se visitan y el rendimiento del sitio.","required":false},
        {"key":"preferences","name":"Preferencias","description":"Recuerdan decisiones como idioma, región u otras configuraciones del sitio.","required":false}
    ]'::jsonb,
    accept_all_label = 'Aceptar todas',
    save_preferences_label = 'Guardar preferencias'
WHERE id = 1;

-- ------------------------------------------------------------------------------
-- 4. legal_esnna — Compromiso contra la ESNNA
-- ------------------------------------------------------------------------------
UPDATE legal_esnna SET
    eyebrow = 'COMPROMISO INSTITUCIONAL / PROTECCIÓN DE LA NIÑEZ',
    title = 'Compromiso contra la ESNNA',
    introduction = 'La presente declaración establece el compromiso de Viajes Carolina frente a la explotación sexual de niñas, niños y adolescentes en el ámbito de los servicios turísticos.',
    document_control_label = 'CONTROL DOCUMENTAL',
    document_control_text = 'DOCUMENTO: COMPROMISO CONTRA LA ESNNA · CÓDIGO: VC-COMP-01 · VERSIÓN: 1.0 · VIGENCIA: AGOSTO 2026 · RESPONSABLE: VIAJES CAROLINA',
    declaration_eyebrow = 'TOLERANCIA CERO',
    declaration_title = 'La protección de niñas, niños y adolescentes no es negociable.',
    declaration_body = 'Este compromiso orienta nuestras decisiones, la relación con proveedores y la manera en que acompañamos a cada viajero.',
    sections_json = '[
        {"title":"Tolerancia cero","body":"Viajes Carolina mantiene tolerancia cero frente a cualquier conducta que implique explotación sexual de niñas, niños o adolescentes. No promovemos, facilitamos ni encubrimos actividades que vulneren sus derechos, incluso cuando involucren clientes, proveedores o terceros vinculados al servicio."},
        {"title":"Prevención","body":"Orientamos al equipo y comunicamos criterios básicos para reconocer señales de riesgo, evitar la normalización de conductas indebidas y actuar sin exponer a la posible víctima. La prevención forma parte de la planificación y del acompañamiento del viaje."},
        {"title":"Proveedores responsables","body":"Priorizamos proveedores que respeten la normativa, mantengan prácticas de protección y colaboren ante alertas. Si conocemos incumplimientos graves, revisamos la relación comercial y adoptamos medidas dentro de nuestras competencias."},
        {"title":"Actuación ante una alerta","body":"Ante una situación sospechosa, protegemos la confidencialidad, evitamos confrontaciones que aumenten el riesgo y comunicamos la información a las autoridades competentes. No realizamos investigaciones por cuenta propia ni difundimos datos sensibles."},
        {"title":"Comunicación responsable","body":"No publicamos ni compartimos imágenes, datos o mensajes que sexualicen, identifiquen o vulneren la dignidad de niñas, niños y adolescentes. La comunicación sobre una alerta debe ser reservada y dirigida a los canales autorizados."},
        {"title":"Compromiso compartido","body":"Viajar también implica cuidar. Invitamos a clientes, proveedores y colaboradores a informarse, observar y reportar responsablemente. La protección de la niñez requiere una actuación conjunta y sostenida."}
    ]'::jsonb,
    closing_title = 'Canal de actuación responsable',
    closing_body = 'Toda alerta debe comunicarse de manera responsable, preservando la identidad y seguridad de la posible víctima. Viajes Carolina actuará dentro de sus competencias y colaborará con las autoridades correspondientes.',
    closing_link_label = 'Consultar compromiso institucional'
WHERE id = 1;

-- ------------------------------------------------------------------------------
-- 5. legal_mincetur — Constancia MINCETUR
-- ------------------------------------------------------------------------------
UPDATE legal_mincetur SET
    eyebrow = 'INFORMACIÓN INSTITUCIONAL / REGISTRO OFICIAL',
    title = 'Constancia MINCETUR',
    introduction = 'Esta página presenta la referencia institucional de Viajes Carolina y permite contrastar sus datos con el canal oficial administrado por MINCETUR.',
    document_control_label = 'CONTROL DOCUMENTAL',
    document_control_text = 'DOCUMENTO: CONSTANCIA MINCETUR · CÓDIGO: VC-REG-01 · VERSIÓN: 1.0 · VIGENCIA: AGOSTO 2026 · RESPONSABLE: VIAJES CAROLINA',
    sections_json = '[
        {"title":"Por qué mostramos esta constancia","body":"Publicamos esta referencia para que el viajero pueda identificar a la empresa con la que coordina, contrastar la información institucional y tomar una decisión informada antes de reservar o efectuar un pago."},
        {"title":"Qué debes revisar","body":"Verifica que el nombre comercial, razón social, número de registro, actividad autorizada y ubicación coincidan con la información entregada durante la atención. Si el portal muestra una fecha de actualización, considera también ese dato."},
        {"title":"Fuente oficial","body":"La consulta se realiza en una plataforma externa administrada por MINCETUR. Viajes Carolina no puede modificar el contenido, la disponibilidad ni los tiempos de actualización del portal oficial."},
        {"title":"Si encuentras una diferencia","body":"Si observas una diferencia, conserva una captura o anota el dato y comunícate con nosotros por los canales publicados en esta web. Solicita la aclaración antes de realizar pagos o entregar documentación personal."}
    ]'::jsonb,
    closing_title = 'Verificación de información oficial',
    closing_body = 'Antes de efectuar una reserva o pago, el viajero debe contrastar la identificación de la agencia con la fuente oficial administrada por MINCETUR. Cualquier diferencia debe aclararse mediante los canales publicados.',
    closing_link_label = 'Consultar registro en MINCETUR',
    verification_eyebrow = 'DATOS OFICIALES DE LA AGENCIA',
    verification_button_label = 'Ver constancia en MINCETUR ↗',
    verification_note = 'Este enlace dirige a una fuente oficial externa.'
WHERE id = 1;
