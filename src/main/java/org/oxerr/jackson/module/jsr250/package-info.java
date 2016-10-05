/**
 * Excludes properties from serialization output.
 *
 * Supports:
 * <ul>
 *     <li>DenyAll</li>
 *     <li>RolesAllowed</li>
 * </ul>
 *
 * For example:
 * <pre>
 * public class User {
 *     private String phone;
 *
 *     &#064;RolesAllowed("ROLE_ADMIN")
 *     public String getPhone() {
 *         return phone;
 *     }
 * }
 * </pre>
 * The phone will be excluded if currently authenticated principal is not in
 * ADMIN role.
 */
package org.oxerr.jackson.module.jsr250;
