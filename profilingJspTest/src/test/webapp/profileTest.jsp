<%@ taglib prefix="profile" uri="/WEB-INF/profile.tld" %>

<%
    // Directly reference the static profiler
    one.xingyi.profile.IProfile profiler=one.xingyi.profilingJsp.ProfilerForTests.profiler;
    pageContext.setAttribute("profiler", profiler);
%>

<profile:profile id="profileTag" profiler="${profiler}">
    <profile:delay/>
</profile:profile>

<profile:start id="startEndProfileTag" profiler="${profiler}"/>
<profile:delay/>
<profile:end id="startEndProfileTag" profiler="${profiler}"/>
